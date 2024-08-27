> [!NOTE]
> New update: 3.4.0: [BlockLines & Animations](https://github.com/unldenis/holoeasy?tab=readme-ov-file#blockline--animations). Scroll down for more information.

HoloEasy is a simple, modern and high-performant Java Minecraft Hologram library for 1.8-1.20.4 servers.

Lightweight replacement for Holographic Display. HoloEasy only uses packets instead of registering the entity in the actual Minecraft server.
<p align="center">
  <img src="preview/gif.gif"  alt="holoeasy video"/>
</p>

## Quickstart

### Requirements 
* ProtocolLib installed on your server

### Add dependency
#### Maven

```xml
<dependency>
    <groupId>com.github.MrXiaoM</groupId>
    <artifactId>holoeasy</artifactId>
    <version>3.4.3-1</version>
</dependency>
```

#### Gradle

```kotlin
implementation("com.github.MrXiaoM:holoeasy:3.4.3-1")

```

Make sure you include the <a href="https://jitpack.io/">repository</a> as well.

## Start programming

### Java

```java
import static org.holoeasy.builder.HologramBuilder.*;

// you can use a Pool or a org.bukkit.Plugin
IHologramPool pool = HoloEasy.startInteractivePool(plugin, 60, 0.5f, 5f);

public void addHologram(Location location) {
    pool.registerHolograms(() -> {
        
        hologram(location, () -> {
            textline("Hello");
            textline("Score {} - {}", 0, 1);
            clickable("Click me").onClick(p -> {
                p.sendTitle(ChatColor.AQUA + "Hi", ChatColor.BOLD + "by HoloEasy",
                        20, 20, 20);
            });
            item(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE));
        });
        // other holograms...
    });

}
```

## Reactive Holograms
From 3.1.0 version, the parameters of text lines and item lines **_can also be reactive_**. This means that you can update the line by simply calling the 'set' method to these.

<p align="center">
  <img src="preview/state.gif"  alt="holoeasy state video"/>
</p>

> [!WARNING]
> Mutable states have no player information at this time. If you need to create a hologram for a specific player, it is recommended that you do **_not_** add it to a Pool.
### Java
```java
registerHolograms(plugin, () -> {
    var clickCount = mutableStateOf(0); // can be any type
    
    var holo = hologram(location, () -> {
        textline("{}!", "Static");
        textline("Count: {}", clickCount);
        clickable("Click me").onClick(player -> clickCount.update(it -> it + 1));
    });
    
    // It hasn't been added to a pool, so it's up to us to make it visible and hide it from players. It's better to use a pool because it's automatic and performs asynchronous operations.
    holo.show(player);
});
```

## BlockLine & Animations
From 3.4.0 version, library added support for:
- BlockLine: Instead of a dropping item it spawns an hologram with the block as helmet.
- Animations: You can apply animations to hologram lines.
<p align="center">
  <img src="preview/blockline-and-animations.gif"  alt="holoeasy state video"/>
</p>

### Java
```java
Iterator<ItemStack> blocks = Arrays.asList(
    new ItemStack(Material.DIRT),
    new ItemStack(Material.IRON_BLOCK),
    new ItemStack(Material.GOLD_BLOCK),
    new ItemStack(Material.DIAMOND_BLOCK)
).iterator();
                    
pool.registerHolograms(() -> {

    Hologram holo = hologram(location, () -> {
        MutableState<ItemStack> currBlock = mutableStateOf(blocks.next()); // can be any type
    
        textline("Hello");
        clickable("Update me").onClick(p -> currBlock.set(blocks.next()));
        block(currBlock);
    });
                
    holo.lineAt(2).setAnimation(Animations.CIRCLE);
});
```
