package org.tanglizi.bmp

import org.tanglizi.bmp.image.BmpImage

object App extends App {
  // example 1
  BmpImage.create(200, 144)
    .fill(255, 0, 0)
    .save("images/blue.bmp")

  // example 2
  val image = BmpImage.fromPath("images/up.bmp")
  image.rotate90(false).save("images/right.bmp")
  image.rotate180().save("images/down.bmp")
  image.mirror().save("images/up_x_mirror.bmp")
  image.mirror(false).save("images/up_y_mirror.bmp")

  println(image.dibHeader)
}
