package by.cyberpunkfandom.barfrontend.data.repositories

import by.cyberpunkfandom.barfrontend.data.mappers.PositionVariantMapper
import by.cyberpunkfandom.barfrontend.data.services.MainService
import by.cyberpunkfandom.barfrontend.domain.PositionVariant

class PositionVariantsRepository(
    private val mainService: MainService,
    private val positionVariantMapper: PositionVariantMapper,
) {

    suspend fun getPositionVariants(
        withNotActive: Boolean = false,
    ): List<PositionVariant> {
        val dtoList = mainService.getPositionVariants()
        return dtoList.map { positionVariantMapper.getDomain(it) }
            .filter { withNotActive || it.isActive }
    }

    suspend fun getPositionVariants(
        positionId: String,
        withNotActive: Boolean = false,
    ): List<PositionVariant> {
        val dtoList = mainService.getPositionVariants(positionId)
        return dtoList.map { positionVariantMapper.getDomain(it) }
            .filter { withNotActive || it.isActive }
    }

    suspend fun setPositionVariantIsActive(
        positionVariantId: String,
        isActive: Boolean,
    ): PositionVariant {
        val dto = mainService.setPositionVariantIsActive(positionVariantId, isActive)
        return positionVariantMapper.getDomain(dto)
    }

    suspend fun createPositionVariant(
        name: String,
        price: Float,
    ): PositionVariant {
        val dto = mainService.createPositionVariant(
            name = name,
            price = price,
        )
        return positionVariantMapper.getDomain(dto)
    }

    suspend fun updatePositionVariant(
        positionVariantId: String,
        name: String?,
        price: Float?,
        isActive: Boolean?,
    ): PositionVariant {
        val dto = mainService.updatePositionVariant(
            positionVariantId = positionVariantId,
            name = name,
            price = price,
            isActive = isActive,
        )
        return positionVariantMapper.getDomain(dto)
    }

    suspend fun deletePositionVariant(positionVariantId: String) {
        mainService.deletePositionVariant(positionVariantId)
    }
}
