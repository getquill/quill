package io.getquill.util

import scala.reflect.macros.whitebox.Context

object InferImplicitValueWithFallback {

  def apply(c: Context)(tpe: c.Type, fallbackTree: c.Tree) = {
    import c.universe._

    def fallback =
      c.typecheck(
        q"""{
          import $fallbackTree._
          _root_.scala.Predef.implicitly[$tpe]
        }""",
        silent = true)

    c.inferImplicitValue(tpe).orElse(fallback) match {
      case EmptyTree => None
      case value     => Some(value)
    }
  }
}
