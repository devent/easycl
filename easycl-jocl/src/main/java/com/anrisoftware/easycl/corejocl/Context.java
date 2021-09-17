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

import static org.jocl.CL.CL_CONTEXT_PLATFORM;
import static org.jocl.CL.clCreateContext;
import static org.jocl.CL.clReleaseContext;

import java.util.function.Supplier;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jocl.cl_context;
import org.jocl.cl_context_properties;
import org.jocl.cl_device_id;
import org.jocl.cl_platform_id;

import com.google.inject.assistedinject.Assisted;

import lombok.extern.slf4j.Slf4j;

/**
 * Wrapper around {@link cl_context}.
 *
 * @author Erwin Müller, {@code <erwin@muellerpublic.de>}
 */
@Slf4j
public class Context implements Supplier<cl_context>, AutoCloseable {

    private cl_context context;

    @Inject
    public Context(@Assisted Supplier<cl_platform_id> platform, @Assisted Supplier<cl_device_id> device) {
        obtainDevice(platform.get(), device.get());
    }

    private void obtainDevice(cl_platform_id platform, cl_device_id device) {
        // Initialize the context properties
        var contextProperties = new cl_context_properties();
        contextProperties.addProperty(CL_CONTEXT_PLATFORM, platform);

        // Create a context for the selected device
        this.context = clCreateContext(contextProperties, 1, new cl_device_id[] { device }, null, null, null);
        log.debug("Created context {}", this);
    }

    @Override
    public cl_context get() {
        return context;
    }

    @Override
    public void close() throws Exception {
        if (context == null) {
            return;
        }
        clReleaseContext(context);
        log.trace("Context released {}", this);
        context = null;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", context).build();
    }
}
