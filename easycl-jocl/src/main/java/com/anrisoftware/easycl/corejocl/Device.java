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

import static org.jocl.CL.CL_DEVICE_TYPE_GPU;
import static org.jocl.CL.clGetDeviceIDs;
import static org.jocl.CL.clReleaseDevice;

import java.util.function.Supplier;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jocl.cl_device_id;
import org.jocl.cl_platform_id;

import com.google.inject.assistedinject.Assisted;

import lombok.extern.slf4j.Slf4j;

/**
 * Wrapper around {@link cl_device_id}.
 * <p>
 * Tries to obtain a GPU device per default.
 *
 * @author Erwin Müller, {@code <erwin@muellerpublic.de>}
 */
@Slf4j
public class Device implements Supplier<cl_device_id>, AutoCloseable {

    private final long deviceType = CL_DEVICE_TYPE_GPU;

    private final int deviceIndex = 0;

    private cl_device_id device;

    /**
     * Obtains a {@link cl_device_id} from the specified platform.
     *
     * @param platform the {@link Supplier} that returns the {@link cl_platform_id}.
     */
    @Inject
    public Device(@Assisted Supplier<cl_platform_id> platform) {
        obtainDevice(platform.get());
    }

    private void obtainDevice(cl_platform_id platform) {
        // Obtain the number of devices for the platform
        int numDevicesArray[] = new int[1];
        clGetDeviceIDs(platform, deviceType, 0, null, numDevicesArray);
        var numDevices = numDevicesArray[0];
        log.debug("Number of devices avaliable {}", numDevices);

        // Obtain a device ID
        cl_device_id devices[] = new cl_device_id[numDevices];
        clGetDeviceIDs(platform, deviceType, numDevices, devices, null);
        this.device = devices[deviceIndex];
        log.debug("Obtained device {}", this);
    }

    @Override
    public cl_device_id get() {
        return device;
    }

    @Override
    public void close() throws Exception {
        if (device == null) {
            return;
        }
        clReleaseDevice(device);
        log.trace("Device released {}", this);
        device = null;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", device).build();
    }
}
