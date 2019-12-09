package org.tanglizi.bmp

import org.tanglizi.bmp.image.BmpImage

/**
 * Hello world!
 *
 */
object App extends App {
  var image: BmpImage = BmpImage.create(200, 144)
  image.fill(255, 0, 0)
  image.save("blue.bmp")

  image = BmpImage.fromPath("up.bmp")
  image.rotate90(false).save("right.bmp")
  image.rotate180().save("down.bmp")
  image.mirror().save("up_x_mirror.bmp")
  image.mirror(false).save("up_y_mirror.bmp")

  println(image.dibHeader.width)
  println(image.dibHeader.height)
  println(image.dibHeader)
  println(image(99, 99))
  println(image(0, 0))
}
