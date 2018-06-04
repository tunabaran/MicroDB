# MicroDB

Low weight SharedPreferences based DB for Android.

## INSTALLATION

Copy MicroDB.java and JsonMapper.java to any package in your project.

**Some android packages doesn't contain this org.json package, you may need to include this dependency**

Add this dependency to App level gradle file.

```

compile group: 'org.json', name: 'json', version: '20180130'

```
**On never versions of gradle**
```

implementation group: 'org.json', name: 'json', version: '20180130'

```
## HOW TO USE

**To use MicroDB first you need create an object instance.**

```

MicroDB microDB = new MicroDB(context);

```

MicroDB uses key value based storage. You need to give keys to objects you save.

You can update/override an object with saving with same key again.

**You can save object with :** 

```

microDB.save("user",user);

```

**You can load object with :**

```

User user = microDB.load("user",User.class);

```

## License

Copyright (C) 2018

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

