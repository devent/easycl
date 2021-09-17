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
package com.anrisoftware.easycl.corejocl;

import static org.jocl.CL.CL_BLOCKING;
import static org.jocl.CL.CL_MEM_READ_ONLY;
import static org.jocl.CL.CL_MEM_READ_WRITE;
import static org.jocl.CL.CL_MEM_SIZE;
import static org.jocl.CL.CL_MEM_USE_HOST_PTR;
import static org.jocl.CL.clCreateBuffer;
import static org.jocl.CL.clEnqueueReadBuffer;
import static org.jocl.CL.clEnqueueWriteBuffer;
import static org.jocl.CL.clGetMemObjectInfo;
import static org.jocl.CL.clReleaseMemObject;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import java.util.List;
import java.util.function.Supplier;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jocl.Pointer;
import org.jocl.Sizeof;
import org.jocl.cl_command_queue;
import org.jocl.cl_context;
import org.jocl.cl_mem;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import lombok.extern.slf4j.Slf4j;

/**
 * Wrapper around {@link cl_mem}.
 *
 * @author Erwin Müller, {@code <erwin@muellerpublic.de>}
 */
@Slf4j
public class Buffer implements HaveContext, Supplier<cl_mem>, AutoCloseable {

    private final cl_context context;

    private cl_mem mem;

    // #################################
    // Generic
    // #################################

    @AssistedInject
    public Buffer(@Assisted Supplier<cl_context> context, @Assisted("flags") long flags, @Assisted("size") long size) {
        this.context = context.get();
        this.mem = clCreateBuffer(context.get(), flags, size, null, null);
        log.debug("Created buffer {}", this);
    }

    @AssistedInject
    public Buffer(@Assisted Supplier<cl_context> context, @Assisted("flags") long flags, @Assisted("size") long size,
            @Assisted Pointer ptr) {
        this.context = context.get();
        this.mem = clCreateBuffer(context.get(), flags, size, ptr, null);
        log.debug("Created buffer {}", this);
    }

    // #################################
    // List
    // #################################

    public static Supplier<cl_mem> fromBytes(BufferFactory bufferFactory, Supplier<cl_context> context,
            Supplier<cl_command_queue> queue, List<Byte> list, boolean readOnly, boolean useHostPtr) {
        var array = ArrayUtils.toPrimitive(list.toArray(new Byte[0]));
        return fromBytes(bufferFactory, context, queue, array, readOnly, useHostPtr);
    }

    public static Supplier<cl_mem> fromChars(BufferFactory bufferFactory, Supplier<cl_context> context,
            Supplier<cl_command_queue> queue, List<Character> list, boolean readOnly, boolean useHostPtr) {
        var array = ArrayUtils.toPrimitive(list.toArray(new Character[0]));
        return fromChars(bufferFactory, context, queue, array, readOnly, useHostPtr);
    }

    public static Supplier<cl_mem> fromDouble(BufferFactory bufferFactory, Supplier<cl_context> context,
            Supplier<cl_command_queue> queue, List<Double> list, boolean readOnly, boolean useHostPtr) {
        var array = ArrayUtils.toPrimitive(list.toArray(new Double[0]));
        return fromDouble(bufferFactory, context, queue, array, readOnly, useHostPtr);
    }

    public static Supplier<cl_mem> fromFloat(BufferFactory bufferFactory, Supplier<cl_context> context,
            Supplier<cl_command_queue> queue, List<Float> list, boolean readOnly, boolean useHostPtr) {
        var array = ArrayUtils.toPrimitive(list.toArray(new Float[0]));
        return fromFloat(bufferFactory, context, queue, array, readOnly, useHostPtr);
    }

    public static Supplier<cl_mem> fromInt(BufferFactory bufferFactory, Supplier<cl_context> context,
            Supplier<cl_command_queue> queue, List<Integer> list, boolean readOnly, boolean useHostPtr) {
        var array = ArrayUtils.toPrimitive(list.toArray(new Integer[0]));
        return fromInt(bufferFactory, context, queue, array, readOnly, useHostPtr);
    }

    public static Supplier<cl_mem> fromLong(BufferFactory bufferFactory, Supplier<cl_context> context,
            Supplier<cl_command_queue> queue, List<Long> list, boolean readOnly, boolean useHostPtr) {
        var array = ArrayUtils.toPrimitive(list.toArray(new Long[0]));
        return fromLong(bufferFactory, context, queue, array, readOnly, useHostPtr);
    }

    public static Supplier<cl_mem> fromShort(BufferFactory bufferFactory, Supplier<cl_context> context,
            Supplier<cl_command_queue> queue, List<Short> list, boolean readOnly, boolean useHostPtr) {
        var array = ArrayUtils.toPrimitive(list.toArray(new Short[0]));
        return fromShort(bufferFactory, context, queue, array, readOnly, useHostPtr);
    }

    // #################################
    // Arrays
    // #################################

    public static Supplier<cl_mem> fromBytes(BufferFactory bufferFactory, Supplier<cl_context> context,
            Supplier<cl_command_queue> queue, byte[] array, boolean readOnly, boolean useHostPtr) {
        return bufferFactory.create(context, queue, ByteBuffer.wrap(array), readOnly, useHostPtr);
    }

    public static Supplier<cl_mem> fromChars(BufferFactory bufferFactory, Supplier<cl_context> context,
            Supplier<cl_command_queue> queue, char[] array, boolean readOnly, boolean useHostPtr) {
        return bufferFactory.create(context, queue, CharBuffer.wrap(array), readOnly, useHostPtr);
    }

    public static Supplier<cl_mem> fromDouble(BufferFactory bufferFactory, Supplier<cl_context> context,
            Supplier<cl_command_queue> queue, double[] array, boolean readOnly, boolean useHostPtr) {
        return bufferFactory.create(context, queue, DoubleBuffer.wrap(array), readOnly, useHostPtr);
    }

    public static Supplier<cl_mem> fromFloat(BufferFactory bufferFactory, Supplier<cl_context> context,
            Supplier<cl_command_queue> queue, float[] array, boolean readOnly, boolean useHostPtr) {
        return bufferFactory.create(context, queue, FloatBuffer.wrap(array), readOnly, useHostPtr);
    }

    public static Supplier<cl_mem> fromInt(BufferFactory bufferFactory, Supplier<cl_context> context,
            Supplier<cl_command_queue> queue, int[] array, boolean readOnly, boolean useHostPtr) {
        return bufferFactory.create(context, queue, IntBuffer.wrap(array), readOnly, useHostPtr);
    }

    public static Supplier<cl_mem> fromLong(BufferFactory bufferFactory, Supplier<cl_context> context,
            Supplier<cl_command_queue> queue, long[] array, boolean readOnly, boolean useHostPtr) {
        return bufferFactory.create(context, queue, LongBuffer.wrap(array), readOnly, useHostPtr);
    }

    public static Supplier<cl_mem> fromShort(BufferFactory bufferFactory, Supplier<cl_context> context,
            Supplier<cl_command_queue> queue, short[] array, boolean readOnly, boolean useHostPtr) {
        return bufferFactory.create(context, queue, ShortBuffer.wrap(array), readOnly, useHostPtr);
    }

    // #################################
    // Native buffers
    // #################################

    @AssistedInject
    public Buffer(@Assisted Supplier<cl_context> context, @Assisted Supplier<cl_command_queue> queue,
            @Assisted ByteBuffer buffer, @Assisted("readOnly") boolean readOnly,
            @Assisted("useHostPtr") boolean useHostPtr) {
        this(context, queue, buffer, Sizeof.cl_char, readOnly, useHostPtr);
    }

    @AssistedInject
    public Buffer(@Assisted Supplier<cl_context> context, @Assisted Supplier<cl_command_queue> queue,
            @Assisted CharBuffer buffer, @Assisted("readOnly") boolean readOnly,
            @Assisted("useHostPtr") boolean useHostPtr) {
        this(context, queue, buffer, Sizeof.cl_char, readOnly, useHostPtr);
    }

    @AssistedInject
    public Buffer(@Assisted Supplier<cl_context> context, @Assisted Supplier<cl_command_queue> queue,
            @Assisted DoubleBuffer buffer, @Assisted("readOnly") boolean readOnly,
            @Assisted("useHostPtr") boolean useHostPtr) {
        this(context, queue, buffer, Sizeof.cl_double, readOnly, useHostPtr);
    }

    @AssistedInject
    public Buffer(@Assisted Supplier<cl_context> context, @Assisted Supplier<cl_command_queue> queue,
            @Assisted FloatBuffer buffer, @Assisted("readOnly") boolean readOnly,
            @Assisted("useHostPtr") boolean useHostPtr) {
        this(context, queue, buffer, Sizeof.cl_float, readOnly, useHostPtr);
    }

    @AssistedInject
    public Buffer(@Assisted Supplier<cl_context> context, @Assisted Supplier<cl_command_queue> queue,
            @Assisted IntBuffer buffer, @Assisted("readOnly") boolean readOnly,
            @Assisted("useHostPtr") boolean useHostPtr) {
        this(context, queue, buffer, Sizeof.cl_int, readOnly, useHostPtr);
    }

    @AssistedInject
    public Buffer(@Assisted Supplier<cl_context> context, @Assisted Supplier<cl_command_queue> queue,
            @Assisted LongBuffer buffer, @Assisted("readOnly") boolean readOnly,
            @Assisted("useHostPtr") boolean useHostPtr) {
        this(context, queue, buffer, Sizeof.cl_long, readOnly, useHostPtr);
    }

    @AssistedInject
    public Buffer(@Assisted Supplier<cl_context> context, @Assisted Supplier<cl_command_queue> queue,
            @Assisted ShortBuffer buffer, @Assisted("readOnly") boolean readOnly,
            @Assisted("useHostPtr") boolean useHostPtr) {
        this(context, queue, buffer, Sizeof.cl_short, readOnly, useHostPtr);
    }

    @AssistedInject
    public Buffer(@Assisted Supplier<cl_context> context, @Assisted Supplier<cl_command_queue> queue,
            @Assisted java.nio.Buffer buffer, @Assisted long sizeType, @Assisted("readOnly") boolean readOnly,
            @Assisted("useHostPtr") boolean useHostPtr) {
        this.context = context.get();
        this.mem = createBuffer(context.get(), queue.get(), readOnly, useHostPtr, sizeType, buffer);
        log.debug("Created buffer {}", this);
    }

    private cl_mem createBuffer(cl_context context, cl_command_queue queue, boolean readOnly, boolean useHostPtr,
            long sizeType, java.nio.Buffer buffer) {
        var flags = 0L;
        if (readOnly) {
            flags |= CL_MEM_READ_ONLY;
        } else {
            flags |= CL_MEM_READ_WRITE;
        }
        if (useHostPtr) {
            flags |= CL_MEM_USE_HOST_PTR;
        }
        cl_mem m;
        var size = sizeType * buffer.capacity();
        if (useHostPtr) {
            var p = Pointer.toBuffer(buffer);
            m = clCreateBuffer(context, flags, size, p, null);
        } else {
            m = clCreateBuffer(context, flags, size, null, null);
        }
        if (!useHostPtr) {
            write(queue, m, 0, size, buffer);
        }
        return m;
    }

    public void read(cl_command_queue queue, float[] dest) {
        read(queue, 0, dest.length, dest);
    }

    public void read(cl_command_queue queue, long offset, long count, float[] dest) {
        read(queue, offset, Sizeof.cl_float * count, Pointer.to(dest));
    }

    public void read(cl_command_queue queue, long offset, long size, java.nio.Buffer dest) {
        read(queue, offset, size, Pointer.toBuffer(dest));
    }

    public void read(cl_command_queue queue, long offset, long size, Pointer dest) {
        clEnqueueReadBuffer(queue, mem, CL_BLOCKING, offset, size, dest, 0, null, null);
    }

    public void write(cl_command_queue queue, long offset, long size, java.nio.Buffer buffer) {
        write(queue, mem, offset, size, Pointer.toBuffer(buffer));
    }

    private void write(cl_command_queue queue, cl_mem mem, long offset, long size, java.nio.Buffer buffer) {
        write(queue, mem, offset, size, Pointer.toBuffer(buffer));
    }

    public void write(cl_command_queue queue, long offset, long size, Pointer src) {
        write(queue, mem, offset, size, src);
    }

    private void write(cl_command_queue queue, cl_mem mem, long offset, long size, Pointer src) {
        clEnqueueWriteBuffer(queue, mem, CL_BLOCKING, offset, size, src, 0, null, null);
    }

    public long retrieveSize() {
        long[] size = { 0 };
        clGetMemObjectInfo(mem, CL_MEM_SIZE, Sizeof.cl_long, Pointer.to(size), null);
        return size[0];
    }

    @Override
    public cl_context getContext() {
        return context;
    }

    @Override
    public cl_mem get() {
        return mem;
    }

    @Override
    public void close() throws Exception {
        if (mem == null) {
            return;
        }
        clReleaseMemObject(mem);
        log.trace("Buffer released {}", this);
        mem = null;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("context", context).append("id", mem).build();
    }

}
