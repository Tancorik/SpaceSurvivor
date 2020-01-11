package com.tancorikworld.spacesurvivor.models.demo.helpers

/**
 * Класс содержит RGB значения цвета.
 *
 * @property red    красная составляющая
 * @property green  зеленая составляющая
 * @property blue   синяя составляющая
 */
data class SimpleColor @JvmOverloads constructor(
    val red: Float,
    val green: Float,
    val blue: Float,
    val alpha: Float = 1f)