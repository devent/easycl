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

import org.jocl.cl_command_queue;
import org.jocl.cl_context;
import org.jocl.cl_device_id;
import org.jocl.cl_kernel;
import org.jocl.cl_mem;
import org.jocl.cl_platform_id;
import org.jocl.cl_program;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * @see PlatformFactory
 * @see DeviceFactory
 * @see ContextFactory
 * @see ProgramFactory
 * @see CommandQueueFactory
 * @see BufferFactory
 * @see KernelFactory
 * @author Erwin Müller, {@code <erwin@muellerpublic.de>}
 */
public class JoclModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().implement(new TypeLiteral<Supplier<cl_platform_id>>() {
        }, Platform.class).build(PlatformFactory.class));
        install(new FactoryModuleBuilder().implement(new TypeLiteral<Supplier<cl_device_id>>() {
        }, Device.class).build(DeviceFactory.class));
        install(new FactoryModuleBuilder().implement(new TypeLiteral<Supplier<cl_context>>() {
        }, Context.class).build(ContextFactory.class));
        install(new FactoryModuleBuilder().implement(new TypeLiteral<Supplier<cl_program>>() {
        }, Program.class).build(ProgramFactory.class));
        install(new FactoryModuleBuilder().implement(new TypeLiteral<Supplier<cl_command_queue>>() {
        }, CommandQueue.class).build(CommandQueueFactory.class));
        install(new FactoryModuleBuilder().implement(new TypeLiteral<Supplier<cl_mem>>() {
        }, Buffer.class).build(BufferFactory.class));
        install(new FactoryModuleBuilder().implement(new TypeLiteral<Supplier<cl_kernel>>() {
        }, Kernel.class).build(KernelFactory.class));
    }
}
