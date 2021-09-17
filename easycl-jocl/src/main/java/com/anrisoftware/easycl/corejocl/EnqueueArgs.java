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

import org.jocl.cl_command_queue;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Global work offsets, global work sizes, local work sizes. Use the same as in
 * <a href="https://github.khronos.org/OpenCL-CLHPP/">OpenCL C++ Bindings.</a>
 *
 * @author Erwin Müller, {@code <erwin@muellerpublic.de>}
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class EnqueueArgs {

    public final cl_command_queue queue;

    public final NDRange offset;

    public final NDRange global;

    public final NDRange local;

    public EnqueueArgs(cl_command_queue queue, NDRange global) {
        this.queue = queue;
        this.global = global;
        this.offset = NDRange.d0Range();
        this.local = NDRange.d0Range();
    }

    public EnqueueArgs(cl_command_queue queue, NDRange global, NDRange local) {
        this.queue = queue;
        this.global = global;
        this.offset = NDRange.d0Range();
        this.local = local;
    }

    public EnqueueArgs(cl_command_queue queue, NDRange offset, NDRange global, NDRange local) {
        this.queue = queue;
        this.global = global;
        this.offset = offset;
        this.local = local;
    }
}
