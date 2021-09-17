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

import static org.jocl.CL.clGetPlatformIDs;

import java.util.function.Supplier;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jocl.cl_platform_id;

import lombok.extern.slf4j.Slf4j;

/**
 * Wrapper around {@link cl_platform_id}.
 *
 * @author Erwin Müller, {@code <erwin@muellerpublic.de>}
 */
@Slf4j
public class Platform implements Supplier<cl_platform_id> {

    private int numPlatforms;

    private final int platformIndex = 0;

    private cl_platform_id platform;

    /**
     * Obtains a OpenCL platform.
     */
    public Platform() {
        retrievePlatforms();
        obtainPlatform();
    }

    private void obtainPlatform() {
        var platforms = new cl_platform_id[numPlatforms];
        clGetPlatformIDs(platforms.length, platforms, null);
        this.platform = platforms[platformIndex];
        log.debug("Obtained platform {}", this);
    }

    private void retrievePlatforms() {
        int[] numPlatformsArray = { 0 };
        clGetPlatformIDs(0, null, numPlatformsArray);
        numPlatforms = numPlatformsArray[0];
        log.debug("Number of platforms avaliable {}", numPlatforms);
    }

    @Override
    public cl_platform_id get() {
        return platform;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", platform).build();
    }
}
