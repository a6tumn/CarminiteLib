@file:Suppress("unused")

package io.autumn.carminite.math

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction.Axis
import kotlin.math.abs

class VoxelBresenhamIterator(
    voxel: BlockPos,
    x2: Int,
    y2: Int,
    z2: Int
) : Iterator<BlockPos>, Iterable<BlockPos> {

    private val xInc: Int
    private val yInc: Int
    private val zInc: Int
    private val doubleAbsDx: Int
    private val doubleAbsDy: Int
    private val doubleAbsDz: Int
    private val length: Int
    private val voxel: BlockPos.MutableBlockPos = voxel.mutable()
    private val direction: Axis

    private var i = 0
    private var err1: Int
    private var err2: Int

    constructor(voxel: BlockPos, towards: BlockPos) :
            this(voxel, towards.x, towards.y, towards.z)

    init {

        val x1 = this.voxel.x
        val y1 = this.voxel.y
        val z1 = this.voxel.z

        val xVec = x2 - x1
        val yVec = y2 - y1
        val zVec = z2 - z1

        val absDx = abs(xVec)
        val absDy = abs(yVec)
        val absDz = abs(zVec)

        xInc = if (xVec < 0) -1 else 1
        yInc = if (yVec < 0) -1 else 1
        zInc = if (zVec < 0) -1 else 1

        doubleAbsDx = absDx shl 1
        doubleAbsDy = absDy shl 1
        doubleAbsDz = absDz shl 1

        when {
            absDx >= absDy && absDx >= absDz -> {
                err1 = doubleAbsDy - absDx
                err2 = doubleAbsDz - absDx
                direction = Axis.X
                length = absDx + 1
            }
            absDy >= absDx && absDy >= absDz -> {
                err1 = doubleAbsDx - absDy
                err2 = doubleAbsDz - absDy
                direction = Axis.Y
                length = absDy + 1
            }
            else -> {
                err1 = doubleAbsDy - absDz
                err2 = doubleAbsDx - absDz
                direction = Axis.Z
                length = absDz + 1
            }
        }
    }

    override fun hasNext(): Boolean = i < length

    override fun next(): BlockPos {
        val out = voxel.immutable()
        if (hasNext()) {
            primeNext()
            i++
        }
        return out
    }

    private fun primeNext() {
        when (direction) {
            Axis.X -> {
                if (err1 > 0) {
                    voxel.move(0, yInc, 0)
                    err1 -= doubleAbsDx
                }
                if (err2 > 0) {
                    voxel.move(0, 0, zInc)
                    err2 -= doubleAbsDx
                }
                err1 += doubleAbsDy
                err2 += doubleAbsDz
                voxel.move(xInc, 0, 0)
            }
            Axis.Y -> {
                if (err1 > 0) {
                    voxel.move(xInc, 0, 0)
                    err1 -= doubleAbsDy
                }
                if (err2 > 0) {
                    voxel.move(0, 0, zInc)
                    err2 -= doubleAbsDy
                }
                err1 += doubleAbsDx
                err2 += doubleAbsDz
                voxel.move(0, yInc, 0)
            }
            Axis.Z -> {
                if (err1 > 0) {
                    voxel.move(0, yInc, 0)
                    err1 -= doubleAbsDz
                }
                if (err2 > 0) {
                    voxel.move(xInc, 0, 0)
                    err2 -= doubleAbsDz
                }
                err1 += doubleAbsDy
                err2 += doubleAbsDx
                voxel.move(0, 0, zInc)
            }
        }
    }

    override fun iterator(): Iterator<BlockPos> = this
}