//
// Created by Titieni Paul on 18.10.2025.
//

#include "Mapper.h"

BatchType Mapper::stringToBatchType(const std::string &type) {
    if (type == "ROWS") {
        return BatchType::ROWS;
    } else if (type == "COLUMNS") {
        return BatchType::COLUMNS;
    } else {
        throw std::invalid_argument("Invalid batch type: " + type);
    }
}

MatrixType Mapper::stringToMatrixType(const std::string &type) {
    if (type == "DYNAMIC") {
        return MatrixType::DYNAMIC;
    } else if (type == "STATIC") {
        return MatrixType::STATIC;
    } else {
        throw std::invalid_argument("Invalid matrix type: " + type);
    }
}

DistributionType Mapper::stringToDistributionType(const std::string &type) {
    if (type == "LINEAR") {
        return DistributionType::LINEAR;
    } else if (type == "CYCLIC") {
        return DistributionType::CYCLIC;
    } else {
        throw std::invalid_argument("Invalid distribution type: " + type);
    }
}

ComputationMethod Mapper::stringToComputationMethod(const std::string &method) {
    if (method == "SEQUENTIAL") {
        return ComputationMethod::SEQUENTIAL;
    } else if (method == "BATCH") {
        return ComputationMethod::BATCH;
    } else if (method == "BLOCK") {
        return ComputationMethod::BLOCK;
    }
    else if (method == "DISTRIBUTION") {
        return ComputationMethod::DISTRIBUTION;
    } else {
        throw std::invalid_argument("Invalid computation method: " + method);
    }
}




