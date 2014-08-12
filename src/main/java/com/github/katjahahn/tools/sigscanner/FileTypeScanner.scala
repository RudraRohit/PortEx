package com.github.katjahahn.tools.sigscanner

import java.io.File
import scala.collection.JavaConverters._
import SignatureScanner.ScanResult
import scala.collection.mutable.ListBuffer
import com.github.katjahahn.parser.IOUtil

class FileTypeScanner(sigscanner: SignatureScanner, file: File) {

  def scanReport(): java.util.List[String] = {
    sigscanner.scanAll(file, false)
  }

  def scan(): List[ScanResult] = {
    sigscanner._scanAll(file, false)
  }

}

object FileTypeScanner {

  private val signatureFile = "customsigs_GCK.txt"

  def main(args: Array[String]): Unit = {
    val file = new File("/home/deque/portextestfiles/WinRar.exe")
    FileTypeScanner(file).scanReport.asScala.foreach(println)
  }

  def apply(file: File): FileTypeScanner = {
    val signatures = loadSignatures()
    val sigscanner = new SignatureScanner(signatures)
    new FileTypeScanner(sigscanner, file)
  }

  private def loadSignatures(): List[Signature] = {
    val sigs = ListBuffer[Signature]()
    val sigArrays = IOUtil.readArray(signatureFile, ",").asScala
    for (array <- sigArrays) {
      val name = array(0)
      val bytes = array(1)
      if (bytes.length() > 8) {
        sigs += Signature(name, false, bytes)
      }
    }
    sigs.toList
  }

}
