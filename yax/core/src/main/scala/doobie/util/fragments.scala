package doobie.util

import doobie.imports._

#+scalaz
import scalaz._, Scalaz._
#-scalaz
#+cats
import cats.{ Reducible => Foldable1, _}, cats.implicits._
#-cats

/** Module of `Fragment` constructors. */
object fragments {

  /** Returns `f IN (fs0, fs1, ...)`. */
  def in[F[_]: Foldable1, A: Param](f: Fragment, fs: F[A]): Fragment =
    fs.toList.map(a => fr0"$a").foldSmash1(f ++ fr0"IN (", fr",", fr")")

  /** Returns `f NOT IN (fs0, fs1, ...)`. */
  def notIn[F[_]: Foldable1, A: Param](f: Fragment, fs: F[A]): Fragment =
    fs.toList.map(a => fr0"$a").foldSmash1(f ++ fr0"NOT IN (", fr",", fr")")

  /** Returns `f1 AND f2 AND ... fn`. */
  def and(fs: Fragment*): Fragment =
    fs.toList.intercalate(fr"AND")

  /** Returns `f1 AND f2 AND ... fn` for all defined fragments. */
  def andOpt(fs: Option[Fragment]*): Fragment =
    and(fs.toList.flatten: _*)

  /** Returns `f1 OR f2 OR ... fn`. */
  def or(fs: Fragment*): Fragment =
    fs.toList.intercalate(fr"OR")

  /** Returns `f1 OR f2 OR ... fn` for all defined fragments. */
  def orOpt(fs: Option[Fragment]*): Fragment =
    or(fs.flatten: _*)

  /** Returns `WHERE f1 AND f2 AND ... fn` or the empty fragment if `fs` is empty. */
  def whereAnd(fs: Fragment*): Fragment =
    if (fs.isEmpty) Fragment.empty else fr"WHERE" ++ and(fs: _*)

  /** Returns `WHERE f1 AND f2 AND ... fn` for defined `f`, if any, otherwise the empty fragment. */
  def whereAndOpt(fs: Option[Fragment]*): Fragment =
    whereAnd(fs.flatten: _*)

  /** Returns `WHERE f1 OR f2 OR ... fn` or the empty fragment if `fs` is empty. */
  def whereOr(fs: Fragment*): Fragment =
    if (fs.isEmpty) Fragment.empty else fr"WHERE" ++ or(fs: _*)

  /** Returns `WHERE f1 OR f2 OR ... fn` for defined `f`, if any, otherwise the empty fragment. */
  def whereOrOpt(fs: Option[Fragment]*): Fragment =
    whereOr(fs.flatten: _*)

}
