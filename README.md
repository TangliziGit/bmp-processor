# bmp-processor
A very simple processor of bitmap in scala, provides reading, operating and writing `.bmp` files functions.  




## Quick Start
```scala
// generate a 200 * 144 bmp file, and fill all pixels in `blue`
var image: BmpImage = BmpImage.create(200, 144)
image.fill(255, 0, 0)
image.save("blue.bmp")

// or you can even do it shorter
BmpImage.create(200, 144)
        .fill(255, 0, 0)
        .save("blue.bmp")
```

```scala
// load a bmp file, names `up.bmp` 
val image = BmpImage.fromPath("up.bmp")

image.rotate90(false).save("right.bmp")
```

```scala
image.rotate180().save("down.bmp")
```

```scala
image.mirror().save("up_x_mirror.bmp")
```

```scala
image.mirror(false).save("up_y_mirror.bmp")
```

![](https://raw.githubusercontent.com/TangliziGit/bmp-processor/master/images/result.png)

```scala
// if you want to see the file structure:
println(image.bmpHeader)
println(image.dibHeader)
```

  

## Reference

<https://en.wikipedia.org/wiki/BMP_file_format>
