package com.example.aquariummanager.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

private const val CM3_TO_LITERS = 0.001
private const val MM3_TO_LITERS = 0.000001
private const val IN3_TO_LITERS = 0.016387
private const val CM3_TO_GALLONS = 0.000264172052
private const val MM3_TO_GALLONS = 0.00000026
private const val IN3_TO_GALLONS = 0.00432900433
private const val LITERS_TO_GALLONS = 0.264172052
private const val GALLONS_TO_LITERS = 3.785411784

class CalculatorViewModel : ViewModel() {
    private var _length = 0.0
    private var _width = 0.0
    private var _height = 0.0
    private var _topIndent = 0.0
    private var _substrateHeight = 0.0
    private var _glassThickness = 0.0
    private var _substrateVolume = MutableLiveData(0.0)
    val substrateVolume: LiveData<Double>
        get() = _substrateVolume
    private var _tankVolume = MutableLiveData(0.0)
    val tankVolume: LiveData<Double>
        get() = _tankVolume
    private var _waterVolume = MutableLiveData(0.0)
    val waterVolume: LiveData<Double>
        get() = _waterVolume
    private var _lengthUnits = "mm"
    private var _volumeUnits = "liters"

    init {
        _tankVolume.value = 0.0
        _waterVolume.value = 0.0
        _substrateVolume.value = 0.0
    }

    /**
     * Vypočítanie všetkých objemov.
     *
     * @param length zadaná dĺžka akvária
     * @param width zadaná šírka akvária
     * @param height zadaná výška akvária
     * @param subs zadaná výška substrátu akvária
     * @param top zadaná výška od hrany po hladinu
     * @param thick zadaná hrúbka skla akvária
     * @param units zadané jednotky
     */
    fun calculateVolumes(length: Double, width: Double, height: Double, subs: Double, top: Double, thick: Double, units:String) {
        _lengthUnits = units;
        _length = length
        _width = width
        _height = height
        _substrateHeight = subs
        _topIndent = top
        _glassThickness = thick
        calculate()
    }

    /**
     * Vypočítanie všetkých objemov a objemu vody.
     *
     */
    private fun calculate(){
        if (_volumeUnits == "liters") {
            when (_lengthUnits) {
                "in" -> _waterVolume.value = waterVolume() * IN3_TO_LITERS
                "mm" -> _waterVolume.value = waterVolume() * MM3_TO_LITERS
                "cm" -> _waterVolume.value = waterVolume() * CM3_TO_LITERS
            }
        } else {
            when (_lengthUnits) {
                "in" -> _waterVolume.value = waterVolume() * IN3_TO_GALLONS
                "mm" -> _waterVolume.value = waterVolume() * MM3_TO_GALLONS
                "cm" -> _waterVolume.value = waterVolume() * CM3_TO_GALLONS
            }
        }
        if (_waterVolume.value!! <= 0.0) {
            _waterVolume.value = 0.0
        }
        calculateSubstrateVolume()
        calculateTankVolume()
    }

    /**
     * Vypočítanie objemu akvária.
     *
     */
    private fun calculateTankVolume() {
        if (_volumeUnits == "liters"){
            when (_lengthUnits) {
                "in" -> _tankVolume.value = tankVolume() * IN3_TO_LITERS
                "mm" -> _tankVolume.value = tankVolume() * MM3_TO_LITERS
                "cm" -> _tankVolume.value = tankVolume() * CM3_TO_LITERS
            }
        } else {
            when (_lengthUnits) {
                "in" -> _tankVolume.value = tankVolume() * IN3_TO_GALLONS
                "mm" -> _tankVolume.value = tankVolume() * MM3_TO_GALLONS
                "cm" -> _tankVolume.value = tankVolume() * CM3_TO_GALLONS
            }
        }
        if (_tankVolume.value!! <= 0.0) {
            _tankVolume.value = 0.0
        }
    }

    /**
     * Vypočítanie všetkých substrátu.
     *
     */
    private fun calculateSubstrateVolume() {
        if (_volumeUnits == "liters"){
            when (_lengthUnits) {
                "in" -> _substrateVolume.value = substrateVolume() * IN3_TO_LITERS
                "mm" -> _substrateVolume.value = substrateVolume() * MM3_TO_LITERS
                "cm" -> _substrateVolume.value = substrateVolume() * CM3_TO_LITERS
            }
        } else {
            when (_lengthUnits) {
                "in" -> _substrateVolume.value = substrateVolume() * IN3_TO_GALLONS
                "mm" -> _substrateVolume.value = substrateVolume() * MM3_TO_GALLONS
                "cm" -> _substrateVolume.value = substrateVolume() * CM3_TO_GALLONS
            }
        }
        if (_substrateVolume.value!! <= 0.0) {
            _substrateVolume.value = 0.0
        }
    }

    /**
     * zmena jedotiek na zadané jednotky
     *
     * @param newUnits nové jednotky
     */
    fun changeLengthUnits(newUnits: String) {
        _lengthUnits = newUnits;
        calculate()
    }

    /**
     * Vracia vypočitaný objem vody
     *
     * @return vypočitaný objem vody
     */
    private fun waterVolume():Double{
        return ((_length - (2.0 * _glassThickness)) * (_width - (2.0 * _glassThickness)) *
                    ((_height - _glassThickness - _topIndent - _substrateHeight)))
    }

    /**
     * Vracia vypočitaný objem akvária
     *
     * @return vypočitaný objem akvária
     */
    private fun tankVolume():Double{
        return  ((_length - (2.0 * _glassThickness)) * (_width - (2.0 * _glassThickness)) *
                    ((_height - _glassThickness)))
    }

    /**
     * Vracia vypočitaný objem substrátu
     *
     * @return vypočitaný objem substrátu
     */
    private fun substrateVolume():Double{
        return ((_length - (2.0 * _glassThickness)) * (_width - (2.0 * _glassThickness)) *
                    ((_substrateHeight)))
    }

    /**
     * Zmena jednotiek objemu podľa parametra
     *
     * @param newUnits nové jednotky objemu
     */
    fun changeVolumeUnits(newUnits: String) {
        if (newUnits == "gallons") {
            _volumeUnits = newUnits
            _tankVolume.value = (_tankVolume.value)?.times(LITERS_TO_GALLONS)
            _waterVolume.value = (_waterVolume.value)?.times(LITERS_TO_GALLONS)
            _substrateVolume.value = (_substrateVolume.value)?.times(LITERS_TO_GALLONS)
        } else if (newUnits == "liters") {
            _volumeUnits = newUnits
            _tankVolume.value = (_tankVolume.value)?.times(GALLONS_TO_LITERS)
            _waterVolume.value = (_waterVolume.value)?.times(GALLONS_TO_LITERS)
            _substrateVolume.value = (_substrateVolume.value)?.times(GALLONS_TO_LITERS)
        }
    }

    /**
     * Vymazanie všetkých údajov
     *
     */
    fun clear(){
        _length = 0.0;
        _width = 0.0;
        _height = 0.0;
        _topIndent = 0.0
        _substrateHeight = 0.0
        _glassThickness = 0.0
        _waterVolume.value = 0.0
        _substrateVolume.value = 0.0
        _tankVolume.value = 0.0
    }
}