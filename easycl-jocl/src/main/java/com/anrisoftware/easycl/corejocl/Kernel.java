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

import static org.jocl.CL.clCreateKernel;
import static org.jocl.CL.clEnqueueNDRangeKernel;
import static org.jocl.CL.clReleaseKernel;
import static org.jocl.CL.clSetKernelArg;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jocl.CLException;
import org.jocl.cl_kernel;
import org.jocl.cl_platform_id;
import org.jocl.cl_program;

import com.google.inject.assistedinject.Assisted;

import lombok.extern.slf4j.Slf4j;

/**
 * Wrapper around {@link cl_kernel}.
 *
 * @author Erwin Müller, {@code <erwin@muellerpublic.de>}
 */
@Slf4j
public class Kernel implements HaveName, Supplier<cl_kernel>, AutoCloseable, Callable<Void> {

    private final String name;

    private cl_kernel kernel;

    private final List<Arg> args;

    private EnqueueArgs enqueueArgs;

    /**
     * Obtains a {@link cl_kernel} from the specified platform.
     *
     * @param platform the {@link Supplier} that returns the {@link cl_platform_id}.
     */
    @Inject
    public Kernel(@Assisted Supplier<cl_program> program, @Assisted String name, @Assisted List<Arg> args) {
        this.name = name;
        this.args = new ArrayList<>(args);
        createKernel(program.get(), name);
    }

    private void createKernel(cl_program program, String name) {
        this.kernel = clCreateKernel(program, name, null);
        log.debug("Created kernel {}", this);
    }

    public Kernel set(int index, byte v) {
        var a = args.get(index);
        clSetKernelArg(kernel, index, a.getSize(v), a.getPointer(v));
        return this;
    }

    public Kernel set(int index, char v) {
        var a = args.get(index);
        clSetKernelArg(kernel, index, a.getSize(v), a.getPointer(v));
        return this;
    }

    public Kernel set(int index, double v) {
        var a = args.get(index);
        clSetKernelArg(kernel, index, a.getSize(v), a.getPointer(v));
        return this;
    }

    public Kernel set(int index, float v) {
        var a = args.get(index);
        clSetKernelArg(kernel, index, a.getSize(v), a.getPointer(v));
        return this;
    }

    public Kernel set(int index, int v) {
        var a = args.get(index);
        clSetKernelArg(kernel, index, a.getSize(v), a.getPointer(v));
        return this;
    }

    public Kernel set(int index, long v) {
        var a = args.get(index);
        clSetKernelArg(kernel, index, a.getSize(v), a.getPointer(v));
        return this;
    }

    public Kernel set(int index, short v) {
        var a = args.get(index);
        clSetKernelArg(kernel, index, a.getSize(v), a.getPointer(v));
        return this;
    }

    public Kernel set(int index, Buffer v) {
        var a = args.get(index);
        clSetKernelArg(kernel, index, a.getSize(v), a.getPointer(v));
        return this;
    }

    public Kernel set(int index, CommandQueue v) {
        var a = args.get(index);
        clSetKernelArg(kernel, index, a.getSize(v), a.getPointer(v));
        return this;
    }

    public Kernel enqueue(EnqueueArgs args) {
        this.enqueueArgs = args;
        return this;
    }

    @Override
    public Void call() throws CLException {
        var offset = enqueueArgs.offset;
        var global = enqueueArgs.global;
        var local = enqueueArgs.local;
        clEnqueueNDRangeKernel(enqueueArgs.queue, kernel, global.dims, offset.dims != 0 ? offset.sizes : null,
                global.sizes, local.dims != 0 ? local.sizes : null, 0, null, null);
        return null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public cl_kernel get() {
        return kernel;
    }

    @Override
    public void close() throws Exception {
        if (kernel == null) {
            return;
        }
        clReleaseKernel(kernel);
        log.trace("Kernel released {}", this);
        kernel = null;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", kernel).build();
    }

}
