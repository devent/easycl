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

import org.jocl.cl_context;
import org.jocl.cl_program;

import com.google.inject.assistedinject.Assisted;

/**
 * Factory to create a new {@link Supplier} that supplies a {@link cl_program}
 * from the specified sources.
 *
 * @author Erwin Müller, {@code <erwin@muellerpublic.de>}
 */
public interface ProgramFactory {

    /**
     * Creates a new {@link Supplier} that supplies a {@link cl_program}.
     *
     * @param context the {@link Supplier} that supplies the {@link cl_context}.
     * @param sources the {@link List} of sources.
     * @param name    the name of the program.
     */
    Supplier<cl_program> create(Supplier<cl_context> context, List<String> sources, @Assisted("name") String name);

    /**
     * Creates a new {@link Supplier} that supplies a {@link cl_program}.
     *
     * @param context the {@link Supplier} that supplies the {@link cl_context}.
     * @param sources the {@link String} of sources.
     * @param name    the name of the program.
     */
    Supplier<cl_program> create(Supplier<cl_context> context, @Assisted("sources") String sources,
            @Assisted("name") String name);

    /**
     * Creates a new {@link Supplier} that supplies a {@link cl_program}.
     *
     * @param context the {@link Supplier} that supplies the {@link cl_context}.
     * @param sources the {@link List} of sources.
     */
    Supplier<cl_program> create(Supplier<cl_context> context, List<String> sources);

    /**
     * Creates a new {@link Supplier} that supplies a {@link cl_program}.
     *
     * @param context the {@link Supplier} that supplies the {@link cl_context}.
     * @param sources the {@link String} of sources.
     */
    Supplier<cl_program> create(Supplier<cl_context> context, @Assisted("sources") String sources);

    /**
     * Creates a new {@link Supplier} that supplies a {@link cl_program}.
     *
     * @param context the {@link cl_context}.
     * @param program the build {@link cl_program}.
     */
    Supplier<cl_program> create(cl_context context, cl_program program);

}
