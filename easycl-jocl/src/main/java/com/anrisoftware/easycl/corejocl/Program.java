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

import static org.apache.commons.lang3.Validate.isTrue;
import static org.jocl.CL.CL_PROGRAM_CONTEXT;
import static org.jocl.CL.clBuildProgram;
import static org.jocl.CL.clCompileProgram;
import static org.jocl.CL.clCreateProgramWithSource;
import static org.jocl.CL.clGetProgramInfo;
import static org.jocl.CL.clLinkProgram;
import static org.jocl.CL.clReleaseProgram;

import java.util.List;
import java.util.function.Supplier;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jocl.Pointer;
import org.jocl.Sizeof;
import org.jocl.cl_context;
import org.jocl.cl_program;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import lombok.extern.slf4j.Slf4j;

/**
 * Wrapper around {@link cl_program}.
 *
 * @author Erwin Müller, {@code <erwin@muellerpublic.de>}
 */
@Slf4j
public class Program implements HaveContext, HaveName, Supplier<cl_program>, AutoCloseable {

    @Inject
    private ProgramFactory programFactory;

    private final cl_context context;

    private final String name;

    private cl_program program;

    private boolean compiled;

    private boolean linked;

    @AssistedInject
    public Program(@Assisted Supplier<cl_context> context, @Assisted List<String> sources) {
        this(context, sources, "");
    }

    @AssistedInject
    public Program(@Assisted Supplier<cl_context> context, @Assisted("sources") String sources) {
        this(context, sources, "");
    }

    @AssistedInject
    public Program(@Assisted Supplier<cl_context> context, @Assisted List<String> sources,
            @Assisted("name") String name) {
        this.context = context.get();
        this.name = name;
        createProgram(context.get(), (String[]) sources.toArray());
    }

    @AssistedInject
    public Program(@Assisted Supplier<cl_context> context, @Assisted("sources") String sources,
            @Assisted("name") String name) {
        this.context = context.get();
        this.name = name;
        createProgram(context.get(), new String[] { sources });
    }

    @AssistedInject
    public Program(@Assisted cl_context context, @Assisted cl_program program) {
        this.context = context;
        this.name = "";
        this.program = program;
    }

    private void createProgram(cl_context context, String[] sources) {
        this.linked = false;
        this.compiled = false;
        this.program = clCreateProgramWithSource(context, sources.length, sources, null, null);
        log.debug("Created program {}", this);
    }

    public Program buildProgram() {
        return buildProgram(null);
    }

    public Program buildProgram(String options) {
        clBuildProgram(program, 0, null, options, null, null);
        return this;
    }

    public Program compileProgram() {
        return compileProgram("", null, null);
    }

    public Program compileProgram(String options) {
        return compileProgram(options, null, null);
    }

    public Program compileProgram(String options, List<Supplier<cl_program>> headers) {
        var prs = new cl_program[headers.size()];
        var names = new String[headers.size()];
        for (var i = 0; i < prs.length; i++) {
            prs[i] = headers.get(i).get();
            names[i] = ((HaveName) headers.get(i)).getName();
        }
        return compileProgram(options, prs, names);
    }

    public Program compileProgram(String options, cl_program[] headers, String[] headerNames) {
        if (compiled) {
            log.warn("Program is already compiled {}", this);
        }
        clCompileProgram(program, 0, null, options, headers == null ? 0 : headers.length, headers, headerNames, null,
                null);
        compiled = true;
        log.debug("Compiled program {}", this);
        return this;
    }

    public Supplier<cl_program> linkLibrary() {
        return linkProgram("-create-library");
    }

    public Supplier<cl_program> linkProgram() {
        return linkProgram(null);
    }

    public Supplier<cl_program> linkProgram(String options) {
        if (linked) {
            log.warn("Program is already linked {}", this);
        }
        isTrue(context.equals(retrieveContext()));
        cl_program[] programs = { program };
        var p = clLinkProgram(context, 0, null, options, 1, programs, null, null, null);
        var ps = programFactory.create(context, p);
        var pp = (Program) ps;
        pp.compiled = true;
        pp.linked = true;
        log.debug("Linked program {}", pp);
        return pp;
    }

    public cl_context retrieveContext() {
        var c = new cl_context();
        var p = Pointer.to(c);
        clGetProgramInfo(program, CL_PROGRAM_CONTEXT, Sizeof.cl_context, p, null);
        log.debug("Retrieved context {} for {}", c, this);
        return c;
    }

    public boolean isCompiled() {
        return compiled;
    }

    public boolean isLinked() {
        return linked;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public cl_context getContext() {
        return context;
    }

    @Override
    public cl_program get() {
        return program;
    }

    @Override
    public void close() throws Exception {
        if (program == null) {
            return;
        }
        clReleaseProgram(program);
        log.trace("Program released {}", this);
        program = null;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("context", context).append("id", program).append("name", name)
                .append("compiled", compiled).append("linked", linked).build();
    }
}
