EasyCL
=== 

Introduction
---

EasyCL aim is to create an easy to use object oriented wrapper around OpenCL.
It is inspired by the [OpenCL C++ bindings.](https://github.khronos.org/OpenCL-CLHPP/)

Examples
---

Short example taken from the test class
`CompileProgramTest.groovy.`

```
def platform = platformFactory.create()
def device = deviceFactory.create(platform)
def context = contextFactory.create(platform, device)
def queue = queueFactory.create(context, device)
def updateGlobal = programFactory.create(context, """
// Taken from the OpenCL C++ Bindings https://github.khronos.org/OpenCL-CLHPP/
global int globalA;
kernel void updateGlobal() {
    globalA = 75;
    printf("%d\\n", globalA);
}
""").buildProgram()
updateGlobal.withCloseable {
    Kernel noargsKernel = kernelFactory.create(updateGlobal, "updateGlobal", [Arg.aint])
    noargsKernel.withCloseable {
        noargsKernel.enqueue(new EnqueueArgs(queue.get(), NDRange.d1Range(1))).call()
    }
}
```

License
---

Copyright (C) 2021 Erwin Müller <erwin@muellerpublic.de>

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http:www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
