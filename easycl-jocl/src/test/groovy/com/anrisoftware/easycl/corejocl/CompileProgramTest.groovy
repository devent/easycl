/**
 * Copyright © 2021 Erwin Müller (erwin@muellerpublic.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.anrisoftware.easycl.corejocl

import static com.anrisoftware.easycl.corejocl.Arg.*
import static org.jocl.CL.*

import java.nio.FloatBuffer

import javax.inject.Inject

import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle
import org.jocl.CL
import org.jocl.Pointer
import org.jocl.Sizeof
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import com.google.inject.Guice
import com.google.inject.Injector

/**
 * Tests create a device and context and compile a library.
 *
 * @author Erwin Müller, {@code <erwin@muellerpublic.de>}
 */
class CompileProgramTest {

    @Inject
    PlatformFactory platformFactory

    @Inject
    DeviceFactory deviceFactory

    @Inject
    ContextFactory contextFactory

    @Inject
    ProgramFactory programFactory

    @Inject
    CommandQueueFactory queueFactory

    @Inject
    BufferFactory bufferFactory

    @Inject
    KernelFactory kernelFactory

    @Test
    void "compile and link program"() {
        def to_close = new ArrayDeque()
        def platform = platformFactory.create()
        def device = deviceFactory.create(platform)
        to_close.push device
        def context = contextFactory.create(platform, device)
        to_close.push context

        def p = programFactory.create(context, """
int util_test_func() {
    return 0;
}
""", "utils.h")
        to_close.push p
        p.compileProgram("-DEASYCL_TEST")

        assert p.compiled
        assert p.retrieveContext() == context.get()

        def lp = p.linkProgram()
        to_close.push lp
        assert lp.compiled
        assert lp.linked

        def lib = p.linkLibrary()
        to_close.push lib
        assert lib.compiled
        assert lib.linked

        to_close.forEach { it.close() }
    }

    @Test
    void "buffers"() {
        def to_close = new ArrayDeque()
        def platform = platformFactory.create()
        def device = deviceFactory.create(platform)
        to_close.push device
        def context = contextFactory.create(platform, device)
        to_close.push context
        def queue = queueFactory.create(context, device)
        to_close.push queue

        long size = Sizeof.cl_float * 8
        def nullBuffer = bufferFactory.create(context, CL_MEM_WRITE_ONLY, size)
        nullBuffer.withCloseable {
            assert nullBuffer.retrieveSize() == size
        }

        float[] values = [0, 1, 2, 3, 4, 5, 6, 7]
        def dataCopiedFlagsBuffer = bufferFactory.create(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, size, Pointer.to(values))
        dataCopiedFlagsBuffer.withCloseable {
            assert dataCopiedFlagsBuffer.retrieveSize() == size
            float[] dest = new float[8];
            dataCopiedFlagsBuffer.read(queue.get(), dest)
            assert dest == values
        }

        def useHostPtrFlagsBuffer = bufferFactory.create(context, CL_MEM_READ_WRITE | CL_MEM_USE_HOST_PTR, size, Pointer.to(values))
        useHostPtrFlagsBuffer.withCloseable {
            assert useHostPtrFlagsBuffer.retrieveSize() == size
            float[] dest = new float[8];
            useHostPtrFlagsBuffer.read(queue.get(), dest)
            assert dest == values
        }

        def buffer = FloatBuffer.wrap(values)
        def copyBuffer = bufferFactory.create(context, queue, buffer, false, false)
        copyBuffer.withCloseable {
            assert copyBuffer.retrieveSize() == size
            float[] dest = new float[8];
            copyBuffer.read(queue.get(), dest)
            assert dest == values
        }

        to_close.forEach { it.close() }
    }

    @Test
    void "run kernel"() {
        def to_close = new ArrayDeque()
        def platform = platformFactory.create()
        def device = deviceFactory.create(platform)
        to_close.push device
        def context = contextFactory.create(platform, device)
        to_close.push context
        def queue = queueFactory.create(context, device)
        to_close.push queue

        def updateGlobal = programFactory.create(context, """
// Taken from the OpenCL C++ Bindings https://github.khronos.org/OpenCL-CLHPP/
global int globalA;
kernel void updateGlobal() {
    globalA = 75;
    printf("%d\\n", globalA);
}
""").buildProgram()
        updateGlobal.withCloseable {
            Kernel noargsKernel = kernelFactory.create(updateGlobal, "updateGlobal", [Arg.aint])
            noargsKernel.withCloseable {
                noargsKernel.enqueue(new EnqueueArgs(queue.get(), NDRange.d1Range(1))).call()
            }
        }

        def vectorAdd = programFactory.create(context, """
// Taken from the OpenCL C++ Bindings https://github.khronos.org/OpenCL-CLHPP/
kernel void vectorAdd(
    global const float* aNum,
    global const float* inputA,
    global const float* inputB,
    global float* output,
    int val,
    queue_t childQueue) {
    output[get_global_id(0)] = inputA[get_global_id(0)] + inputB[get_global_id(0)] + val + *(aNum);
    queue_t default_queue = get_default_queue();
    ndrange_t ndrange = ndrange_1D(get_global_size(0)/2, get_global_size(0)/2);
    // Have a child kernel write into third quarter of output
    enqueue_kernel(default_queue, CLK_ENQUEUE_FLAGS_WAIT_KERNEL, ndrange,
      ^{
          output[get_global_size(0)*2 + get_global_id(0)] =
            inputA[get_global_size(0)*2 + get_global_id(0)] + inputB[get_global_size(0)*2 + get_global_id(0)];
      });
    // Have a child kernel write into last quarter of output
    enqueue_kernel(childQueue, CLK_ENQUEUE_FLAGS_WAIT_KERNEL, ndrange,
      ^{
          output[get_global_size(0)*3 + get_global_id(0)] =
            inputA[get_global_size(0)*3 + get_global_id(0)] + inputB[get_global_size(0)*3 + get_global_id(0)] + 2;
      });
}
""").buildProgram()
        vectorAdd.withCloseable {
            Kernel argsKernel = kernelFactory.create(vectorAdd, "vectorAdd", [
                abuffer,
                abuffer,
                abuffer,
                abuffer,
                aint,
                aqueue
            ])
            def to_close1 = new ArrayDeque()
            int count = 32
            int val = 25
            def aNumBuff = Buffer.fromFloat(bufferFactory, context, queue, ([(Float)9] * count) as List, false, false)
            to_close1.push aNumBuff
            def inputABuff = Buffer.fromFloat(bufferFactory, context, queue, ([(Float)1] * count) as float[], false, false)
            to_close1.push inputABuff
            def inputBBuff = Buffer.fromFloat(bufferFactory, context, queue, ([(Float)2] * count) as float[], false, false)
            to_close1.push inputBBuff
            def outputBuff = bufferFactory.create(context, CL_MEM_WRITE_ONLY, Sizeof.cl_float * count)
            to_close1.push outputBuff
            argsKernel.set(0, aNumBuff).set(1, inputABuff).set(2, inputBBuff).set(3, outputBuff).set(4, val).set(5, queue)
            argsKernel.withCloseable {
                argsKernel.enqueue(new EnqueueArgs(queue.get(), NDRange.d1Range((count / 2) as long), NDRange.d1Range((count / 2) as long))).call()
            }
            to_close1.forEach { it.close() }
        }

        to_close.forEach { it.close() }
    }

    @BeforeEach
    void injectDeps() {
        injector.injectMembers(this)
    }

    static Injector injector

    @BeforeAll
    static void createInjector() {
        CL.exceptionsEnabled = true
        ToStringBuilder.setDefaultStyle(ToStringStyle.SHORT_PREFIX_STYLE)
        injector = Guice.createInjector(new JoclModule())
    }
}
