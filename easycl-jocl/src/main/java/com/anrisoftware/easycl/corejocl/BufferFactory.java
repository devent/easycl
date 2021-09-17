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

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import java.util.function.Supplier;

import org.jocl.Pointer;
import org.jocl.cl_command_queue;
import org.jocl.cl_context;
import org.jocl.cl_mem;

import com.google.inject.assistedinject.Assisted;

/**
 * Factory to create a new {@link Supplier} that supplies a {@link cl_mem} from
 * the specified sources.
 *
 * @author Erwin Müller, {@code <erwin@muellerpublic.de>}
 */
public interface BufferFactory {

    /**
     * Creates a new {@link Supplier} that supplies a {@link cl_mem}.
     *
     * @param context the {@link Supplier} that supplies the {@link cl_context}.
     * @param flags   the buffer flags.
     * @param size    the size of the buffer.
     */
    Supplier<cl_mem> create(@Assisted Supplier<cl_context> context, @Assisted("flags") long flags,
            @Assisted("size") long size);

    /**
     * Creates a new {@link Supplier} that supplies a {@link cl_mem}.
     *
     * @param context the {@link Supplier} that supplies the {@link cl_context}.
     * @param flags   the buffer flags.
     * @param size    the size of the buffer.
     * @param ptr     the {@link Pointer} to the data.
     */
    Supplier<cl_mem> create(@Assisted Supplier<cl_context> context, @Assisted("flags") long flags,
            @Assisted("size") long size, @Assisted Pointer ptr);

    /**
     * Creates a new {@link Supplier} that supplies a {@link cl_mem}.
     *
     * @param context    the {@link Supplier} that supplies the {@link cl_context}.
     * @param queue      the {@link Supplier} that supplies the
     *                   {@link cl_command_queue}. Is only needed if
     *                   <code>useHostPtr</code> is set to <code>false</code> so
     *                   that the data can be copied to the buffer from the
     *                   specified <code>buffer</code>. If <code>useHostPtr</code>
     *                   is set to <code>true</code> then the
     *                   {@link CommandQueue#nullQueue} NOP supplied can used.
     * @param buffer     the {@link ByteBuffer} with the data.
     * @param readOnly   set to <code>true</code> to have read only buffer.
     * @param useHostPtr set to <code>true</code> to use the host pointer for the
     *                   buffer.
     */
    Supplier<cl_mem> create(@Assisted Supplier<cl_context> context, @Assisted Supplier<cl_command_queue> queue,
            @Assisted ByteBuffer buffer, @Assisted("readOnly") boolean readOnly,
            @Assisted("useHostPtr") boolean useHostPtr);

    /**
     * Creates a new {@link Supplier} that supplies a {@link cl_mem}.
     *
     * @param context    the {@link Supplier} that supplies the {@link cl_context}.
     * @param queue      the {@link Supplier} that supplies the
     *                   {@link cl_command_queue}. Is only needed if
     *                   <code>useHostPtr</code> is set to <code>false</code> so
     *                   that the data can be copied to the buffer from the
     *                   specified <code>buffer</code>. If <code>useHostPtr</code>
     *                   is set to <code>true</code> then the
     *                   {@link CommandQueue#nullQueue} NOP supplied can used.
     * @param buffer     the {@link CharBuffer} with the data.
     * @param readOnly   set to <code>true</code> to have read only buffer.
     * @param useHostPtr set to <code>true</code> to use the host pointer for the
     *                   buffer.
     */
    Supplier<cl_mem> create(@Assisted Supplier<cl_context> context, @Assisted Supplier<cl_command_queue> queue,
            @Assisted CharBuffer buffer, @Assisted("readOnly") boolean readOnly,
            @Assisted("useHostPtr") boolean useHostPtr);

    /**
     * Creates a new {@link Supplier} that supplies a {@link cl_mem}.
     *
     * @param context    the {@link Supplier} that supplies the {@link cl_context}.
     * @param queue      the {@link Supplier} that supplies the
     *                   {@link cl_command_queue}. Is only needed if
     *                   <code>useHostPtr</code> is set to <code>false</code> so
     *                   that the data can be copied to the buffer from the
     *                   specified <code>buffer</code>. If <code>useHostPtr</code>
     *                   is set to <code>true</code> then the
     *                   {@link CommandQueue#nullQueue} NOP supplied can used.
     * @param buffer     the {@link DoubleBuffer} with the data.
     * @param readOnly   set to <code>true</code> to have read only buffer.
     * @param useHostPtr set to <code>true</code> to use the host pointer for the
     *                   buffer.
     */
    Supplier<cl_mem> create(@Assisted Supplier<cl_context> context, @Assisted Supplier<cl_command_queue> queue,
            @Assisted DoubleBuffer buffer, @Assisted("readOnly") boolean readOnly,
            @Assisted("useHostPtr") boolean useHostPtr);

    /**
     * Creates a new {@link Supplier} that supplies a {@link cl_mem}.
     *
     * @param context    the {@link Supplier} that supplies the {@link cl_context}.
     * @param queue      the {@link Supplier} that supplies the
     *                   {@link cl_command_queue}. Is only needed if
     *                   <code>useHostPtr</code> is set to <code>false</code> so
     *                   that the data can be copied to the buffer from the
     *                   specified <code>buffer</code>. If <code>useHostPtr</code>
     *                   is set to <code>true</code> then the
     *                   {@link CommandQueue#nullQueue} NOP supplied can used.
     * @param buffer     the {@link FloatBuffer} with the data.
     * @param readOnly   set to <code>true</code> to have read only buffer.
     * @param useHostPtr set to <code>true</code> to use the host pointer for the
     *                   buffer.
     */
    Supplier<cl_mem> create(@Assisted Supplier<cl_context> context, @Assisted Supplier<cl_command_queue> queue,
            @Assisted FloatBuffer buffer, @Assisted("readOnly") boolean readOnly,
            @Assisted("useHostPtr") boolean useHostPtr);

    /**
     * Creates a new {@link Supplier} that supplies a {@link cl_mem}.
     *
     * @param context    the {@link Supplier} that supplies the {@link cl_context}.
     * @param queue      the {@link Supplier} that supplies the
     *                   {@link cl_command_queue}. Is only needed if
     *                   <code>useHostPtr</code> is set to <code>false</code> so
     *                   that the data can be copied to the buffer from the
     *                   specified <code>buffer</code>. If <code>useHostPtr</code>
     *                   is set to <code>true</code> then the
     *                   {@link CommandQueue#nullQueue} NOP supplied can used.
     * @param buffer     the {@link IntBuffer} with the data.
     * @param readOnly   set to <code>true</code> to have read only buffer.
     * @param useHostPtr set to <code>true</code> to use the host pointer for the
     *                   buffer.
     */
    Supplier<cl_mem> create(@Assisted Supplier<cl_context> context, @Assisted Supplier<cl_command_queue> queue,
            @Assisted IntBuffer buffer, @Assisted("readOnly") boolean readOnly,
            @Assisted("useHostPtr") boolean useHostPtr);

    /**
     * Creates a new {@link Supplier} that supplies a {@link cl_mem}.
     *
     * @param context    the {@link Supplier} that supplies the {@link cl_context}.
     * @param queue      the {@link Supplier} that supplies the
     *                   {@link cl_command_queue}. Is only needed if
     *                   <code>useHostPtr</code> is set to <code>false</code> so
     *                   that the data can be copied to the buffer from the
     *                   specified <code>buffer</code>. If <code>useHostPtr</code>
     *                   is set to <code>true</code> then the
     *                   {@link CommandQueue#nullQueue} NOP supplied can used.
     * @param buffer     the {@link LongBuffer} with the data.
     * @param readOnly   set to <code>true</code> to have read only buffer.
     * @param useHostPtr set to <code>true</code> to use the host pointer for the
     *                   buffer.
     */
    Supplier<cl_mem> create(@Assisted Supplier<cl_context> context, @Assisted Supplier<cl_command_queue> queue,
            @Assisted LongBuffer buffer, @Assisted("readOnly") boolean readOnly,
            @Assisted("useHostPtr") boolean useHostPtr);

    /**
     * Creates a new {@link Supplier} that supplies a {@link cl_mem}.
     *
     * @param context    the {@link Supplier} that supplies the {@link cl_context}.
     * @param queue      the {@link Supplier} that supplies the
     *                   {@link cl_command_queue}. Is only needed if
     *                   <code>useHostPtr</code> is set to <code>false</code> so
     *                   that the data can be copied to the buffer from the
     *                   specified <code>buffer</code>. If <code>useHostPtr</code>
     *                   is set to <code>true</code> then the
     *                   {@link CommandQueue#nullQueue} NOP supplied can used.
     * @param buffer     the {@link ShortBuffer} with the data.
     * @param readOnly   set to <code>true</code> to have read only buffer.
     * @param useHostPtr set to <code>true</code> to use the host pointer for the
     *                   buffer.
     */
    Supplier<cl_mem> create(@Assisted Supplier<cl_context> context, @Assisted Supplier<cl_command_queue> queue,
            @Assisted ShortBuffer buffer, @Assisted("readOnly") boolean readOnly,
            @Assisted("useHostPtr") boolean useHostPtr);
}
