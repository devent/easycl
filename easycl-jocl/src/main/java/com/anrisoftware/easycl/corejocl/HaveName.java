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

import org.jocl.CL;

/**
 * OpenCL resource with a name.
 * <p>
 * A {@link Program} can have a name. The name will be used as the header name
 * in
 * {@link CL#clCompileProgram(org.jocl.cl_program, int, org.jocl.cl_device_id[], String, int, org.jocl.cl_program[], String[], org.jocl.BuildProgramFunction, Object)}
 *
 * @author Erwin Müller, {@code <erwin@muellerpublic.de>}
 */
public interface HaveName {

    String getName();
}
