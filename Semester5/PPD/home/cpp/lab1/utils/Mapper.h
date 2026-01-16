//
// Created by Titieni Paul on 18.10.2025.
//

#ifndef LAB1_MAPPER_H
#define LAB1_MAPPER_H


#include <string>
#include "../constants/MatrixType.h"
#include "../constants/BatchType.h"
#include "../constants/DistributionType.h"
#include "../constants/ComputationMethod.h"

class Mapper {
public:
    static MatrixType stringToMatrixType(const std::string& type);

    static BatchType stringToBatchType(const std::string& type);

    static DistributionType stringToDistributionType(const std::string& type);

    static ComputationMethod stringToComputationMethod(const std::string& method);

};


#endif //LAB1_MAPPER_H
