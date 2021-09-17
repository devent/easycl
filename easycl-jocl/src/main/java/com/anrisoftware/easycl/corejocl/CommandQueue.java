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

import static org.jocl.CL.clCreateCommandQueueWithProperties;
import static org.jocl.CL.clReleaseCommandQueue;

import java.util.function.Supplier;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jocl.cl_command_queue;
import org.jocl.cl_context;
import org.jocl.cl_device_id;
import org.jocl.cl_queue_properties;

import com.google.inject.assistedinject.Assisted;

import lombok.extern.slf4j.Slf4j;

/**
 * Wrapper around {@link cl_command_queue}.
 *
 * @author Erwin Müller, {@code <erwin@muellerpublic.de>}
 */
@Slf4j
public class CommandQueue implements Supplier<cl_command_queue>, AutoCloseable {

    public static final Supplier<cl_command_queue> nullQueue = () -> {
        return null;
    };

    private cl_command_queue queue;

    @Inject
    public CommandQueue(@Assisted Supplier<cl_context> context, @Assisted Supplier<cl_device_id> device) {
        createQueue(context.get(), device.get());
    }

    private void createQueue(cl_context context, cl_device_id device) {
        var properties = new cl_queue_properties();
        this.queue = clCreateCommandQueueWithProperties(context, device, properties, null);
        log.debug("Created command queue {}", this);
    }

    @Override
    public cl_command_queue get() {
        return queue;
    }

    @Override
    public void close() throws Exception {
        if (queue == null) {
            return;
        }
        clReleaseCommandQueue(queue);
        log.trace("Command queue released {}", this);
        queue = null;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", queue).build();
    }
}
