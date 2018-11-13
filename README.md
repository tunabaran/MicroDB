# MicroDB

Low weight SharedPreferences based DB for Android.

## INSTALLATION

1-) Add repository to Project level gradle file.
```

allprojects {
    repositories {
        google()
        jcenter()
        maven { url "http://microdb.s3.amazonaws.com" }
    }
}

```

2-)Add dependency to App level gradle file.
```

implementation (group: 'com.tunabaranurut', name: 'microdb', version: '0.2.11', ext: 'aar', classifier: 'debug'){
    transitive = true
}

```
## HOW TO USE

**To use MicroDB first you need create an object instance.**

```

MicroDB microDB = new MicroDB(context);

```

MicroDB uses key value based storage. You need to give keys to objects you save.

You can update/override an object with saving with same key again.

**Save object with :** 

```

microDB.save("user",user);

```

**Load object with :**

```

User user = microDB.load("user",User.class);

```

**Delete object with :**

```

User user = microDB.delete("user");

```

**Get saved object keys with :**

```

microDB.keySet();

```

**Clear database:**

```

microDB.clear();

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

