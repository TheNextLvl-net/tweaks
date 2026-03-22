-dontshrink
-dontoptimize

# Keep the main plugin class
-keep class net.thenextlvl.tweaks.TweaksPlugin { *; }

# Keep all Bukkit listeners (registered by reflection)
-keep class * implements org.bukkit.event.Listener { *; }

# Keep model classes used by Gson (reflection-based serialization)
-keep class net.thenextlvl.tweaks.model.** { *; }
-keep class net.thenextlvl.tweaks.adapter.** { *; }

# Keep Gson annotations
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod

# Keep bStats and metrics (relocated by shadow)
-keep class net.thenextlvl.tweaks.bstats.** { *; }

# Don't warn about missing library classes (provided by the server at runtime)
-dontwarn **
-ignorewarnings

# Keep source file names for stack traces
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Output obfuscation mappings
-printmapping build/obfuscation-mappings.txt
