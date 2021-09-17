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

import org.jocl.Pointer;
import org.jocl.Sizeof;

/**
 * Wrapper around a kernel argument.
 *
 * @author Erwin Müller, {@code <erwin@muellerpublic.de>}
 */
public abstract class Arg {

    public static final Arg abyte = new Arg() {

        @Override
        public long getSize(Object value) {
            return Sizeof.cl_char;
        }

        @Override
        public Pointer getPointer(Object value) {
            throw new IllegalArgumentException();
        }

    };

    public static final Arg achar = new Arg() {

        @Override
        public long getSize(Object value) {
            return Sizeof.cl_char;
        }

        @Override
        public Pointer getPointer(Object value) {
            throw new IllegalArgumentException();
        }

    };

    public static final Arg adouble = new Arg() {

        @Override
        public long getSize(Object value) {
            return Sizeof.cl_double;
        }

        @Override
        public Pointer getPointer(Object value) {
            throw new IllegalArgumentException();
        }

    };

    public static final Arg afloat = new Arg() {

        @Override
        public long getSize(Object value) {
            return Sizeof.cl_float;
        }

        @Override
        public Pointer getPointer(Object value) {
            throw new IllegalArgumentException();
        }

    };

    public static final Arg aint = new Arg() {

        @Override
        public long getSize(Object value) {
            return Sizeof.cl_int;
        }

        @Override
        public Pointer getPointer(Object value) {
            throw new IllegalArgumentException();
        }

    };

    public static final Arg along = new Arg() {

        @Override
        public long getSize(Object value) {
            return Sizeof.cl_long;
        }

        @Override
        public Pointer getPointer(Object value) {
            throw new IllegalArgumentException();
        }

    };

    public static final Arg ashort = new Arg() {

        @Override
        public long getSize(Object value) {
            return Sizeof.cl_short;
        }

        @Override
        public Pointer getPointer(Object value) {
            throw new IllegalArgumentException();
        }

    };

    public static final Arg abuffer = new Arg() {

        @Override
        public boolean checkArg(Object value) {
            return value instanceof Buffer;
        }

        @Override
        public long getSize(Object value) {
            return Sizeof.cl_mem;
        }

        @Override
        public Pointer getPointer(Object value) {
            var buffer = (Buffer) value;
            return Pointer.to(buffer.get());
        }

    };

    public static final Arg aqueue = new Arg() {

        @Override
        public boolean checkArg(Object value) {
            return value instanceof CommandQueue;
        }

        @Override
        public long getSize(Object value) {
            return Sizeof.cl_command_queue;
        }

        @Override
        public Pointer getPointer(Object value) {
            var q = (CommandQueue) value;
            return Pointer.to(q.get());
        }

    };

    public Pointer getPointer(byte value) {
        byte[] values = { value };
        return Pointer.to(values);
    }

    public Pointer getPointer(char value) {
        char[] values = { value };
        return Pointer.to(values);
    }

    public Pointer getPointer(double value) {
        double[] values = { value };
        return Pointer.to(values);
    }

    public Pointer getPointer(float value) {
        float[] values = { value };
        return Pointer.to(values);
    }

    public Pointer getPointer(int value) {
        int[] values = { value };
        return Pointer.to(values);
    }

    public Pointer getPointer(long value) {
        long[] values = { value };
        return Pointer.to(values);
    }

    public Pointer getPointer(short value) {
        short[] values = { value };
        return Pointer.to(values);
    }

    public boolean checkArg(Object value) {
        return true;
    }

    public abstract long getSize(Object value);

    public abstract Pointer getPointer(Object value);
}
