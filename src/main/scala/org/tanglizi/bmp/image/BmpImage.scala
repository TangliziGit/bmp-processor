package org.tanglizi.bmp.image

import java.io.{DataInputStream, File, FileOutputStream}
import java.nio.ByteBuffer

class BmpImage(val bmpHeader: BmpHeader,
               val dibHeader: DibHeader,
               var colorTable: Seq[Seq[(Short, Short, Short)]],
              ) {

  def apply(x: Int, y: Int): (Short, Short, Short) = colorTable(y)(x)

  def save(path: String): Unit ={
    save(new File(path))
  }

  def save(file: File): Unit = {
    val bytes = BmpWriter.toBytes(this)
    val stream = new FileOutputStream(file)
    stream.write(bytes.toArray)
    stream.close()
  }

  // --------------- Image Conversation Operations ---------------

  def mirror(xAxis: Boolean = true): BmpImage = xAxis match {
    case true => xMirror()
    case _ => yMirror()
  }

  private[this] def xMirror(): BmpImage = {
    val colorTable = {
      for (y <- 0 until this.colorTable.length) yield
          for (x <- (0 until this.colorTable(0).length).reverse) yield
          this.colorTable(y)(x)
    }

    /** the headers are immutable */
    new BmpImage(this.bmpHeader, this.dibHeader, colorTable)
  }

  private[this] def yMirror(): BmpImage = {
    val colorTable = {
      for (y <- (0 until this.colorTable.length).reverse) yield
        this.colorTable(y)
    }

    /** the headers are immutable */
    new BmpImage(this.bmpHeader, this.dibHeader, colorTable)
  }

  def rotate90(antiClock: Boolean = true): BmpImage = {
    val dibHeader: DibHeader = DibHeader.changeFrom(this.dibHeader, this.dibHeader.height, this.dibHeader.width)
    val colorTable = {
      val maxy = this.colorTable.length
      val maxx = this.colorTable(0).length
      for (y <- 0 until maxx) yield
        for (x <- 0 until maxy) yield
          if (antiClock) this.colorTable(maxy - x - 1)(y)
          else this.colorTable(x)(maxx - y - 1)
    }

    /** the headers are immutable */
    new BmpImage(this.bmpHeader, dibHeader, colorTable)
  }

  def rotate180(): BmpImage =
    rotate90().rotate90()

  // --------------- Image Drawing Operations ---------------

  def fill(color: (Short, Short, Short)): BmpImage = new BmpImage(
    this.bmpHeader, this.dibHeader,
    Seq.fill(colorTable.length, colorTable(0).length)(color)
  )
}

object BmpImage {
  def open(path: String) = fromPath(path)
  def fromPath(path: String) = fromFile(new File(path))
  def fromFile(file: File) = fromBuffer(BmpUtil.toBuffer(file))
  def fromStream(stream: DataInputStream): BmpImage = BmpReader.fromStream(stream)
  def fromBuffer(buffer: ByteBuffer):BmpImage = BmpReader.fromBuffer(buffer)

  def create(colorTable: Seq[Seq[(Short, Short, Short)]]): BmpImage = {
    val dibHeader: DibHeader = DibHeader.useDefault(colorTable(0).length, colorTable.length)
    val bmpHeader: BmpHeader = BmpHeader.of(dibHeader, colorTable)

    new BmpImage(bmpHeader, dibHeader, colorTable)
  }

  def create(width: Long, height: Long): BmpImage = {
    val colorTable: Seq[Seq[(Short, Short, Short)]] = Seq.fill(width.toInt, height.toInt){(0, 0, 0)}
    val dibHeader: DibHeader = DibHeader.useDefault(width, height)
    val bmpHeader: BmpHeader = BmpHeader.of(dibHeader, colorTable)

    new BmpImage(bmpHeader, dibHeader, colorTable)
  }

}

