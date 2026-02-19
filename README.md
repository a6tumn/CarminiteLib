
<img width="2048" height="345" alt="minecraft_title" src="https://github.com/user-attachments/assets/edc697eb-039c-46ba-8812-dc40078b748e" />


---

## Info

- This library is built to help streamline both boilerplate and complicated code in Minecraft Fabric modding. The library is entirely written in Kotlin, which is the language I prefer writing mods in due to its simplicity and readability compared to Java. The main use case of these APIs will be to support my writing of the Twilight Forest mod for the Fabric mod loader, however, other mod authors are welcome to make use of it in their own mods.

You can check out my Twilight Forest mod [HERE](https://github.com/shinigami7x/TwilightForest).

---

## Licensing

- All code is released under the **GNU GPL v3.0** license [HERE](LICENSE).

See the `LICENSE` file for full details.

---

## Current Version

- *Minecraft* : `26.1-snapshot-9`
- *Library* : `1.0.1`

---

## 📦 Maven

- If you want to include this library in your mod, add the following to your build script.

### Kotlin DSL (build.gradle.kts)

```kotlin
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/shinigami7x/Carminite")
    }
}

dependencies {
    implementation("io.autumn:carminite-blockproperties-api-v1:1.0.1")
}
```

---

### Groovy DSL (build.gradle)

```groovy
repositories {
  maven {
    url "https://maven.pkg.github.com/shinigami7x/Carminite"
  }
}

dependencies {
  implementation "io.autumn:carminite-blockproperties-api-v1:1.0.1"
}
```


## 📫 Contact Info

- Email: `shinigami7x@proton.me`
- Discord: `chronictsuki`  
  *(Discord may not be checked frequently.)*

