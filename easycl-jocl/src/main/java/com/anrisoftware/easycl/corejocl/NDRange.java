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

import lombok.Data;

/**
 * NDRange values. Use the same as in
 * <a href="https://github.khronos.org/OpenCL-CLHPP/">OpenCL C++ Bindings.</a>
 *
 * @author Erwin Müller, {@code <erwin@muellerpublic.de>}
 */
@Data
public class NDRange {

    public static NDRange d0Range() {
        return new NDRange(0, new long[] { 0, 0, 0 });
    }

    public static NDRange d1Range(long size0) {
        return new NDRange(1, new long[] { size0, 1, 1 });
    }

    public static NDRange d2Range(long size0, long size1) {
        return new NDRange(1, new long[] { size0, size1, 1 });
    }

    public static NDRange d3Range(long size0, long size1, long size2) {
        return new NDRange(1, new long[] { size0, size1, size2 });
    }

    public final int dims;

    public final long[] sizes;
}
