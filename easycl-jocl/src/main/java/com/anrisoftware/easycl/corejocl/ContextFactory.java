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

import java.util.function.Supplier;

import org.jocl.cl_context;
import org.jocl.cl_device_id;
import org.jocl.cl_platform_id;

/**
 * Factory to create a new {@link Supplier} that supplies a {@link cl_context}
 * for the specified platform and device.
 *
 * @author Erwin Müller, {@code <erwin@muellerpublic.de>}
 */
public interface ContextFactory {

    /**
     * Creates a new {@link Supplier} that supplies a {@link cl_context}.
     *
     * @param platform the {@link Supplier} that returns the {@link cl_platform_id}.
     * @param device   the {@link Supplier} that returns the {@link cl_device_id}.
     */
    Supplier<cl_context> create(Supplier<cl_platform_id> platform, Supplier<cl_device_id> device);
}