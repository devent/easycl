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

import java.util.List;
import java.util.function.Supplier;

import org.jocl.cl_kernel;
import org.jocl.cl_program;

/**
 * Factory to create a new {@link Supplier} that supplies a {@link cl_kernel}
 * for the specified platform.
 *
 * @author Erwin Müller, {@code <erwin@muellerpublic.de>}
 */
public interface KernelFactory {

    /**
     * Creates a new {@link Supplier} that supplies a {@link cl_kernel} for the
     * specified platform.
     *
     * @param program the {@link Supplier} that returns the {@link cl_program}.
     */
    Supplier<cl_kernel> create(Supplier<cl_program> program, String name, List<Arg> args);
}
