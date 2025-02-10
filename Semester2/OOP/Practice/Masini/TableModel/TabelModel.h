//
// Created by Titieni Paul on 25.06.2024.
//

#ifndef MASINI_TABELMODEL_H
#define MASINI_TABELMODEL_H

#include <QAbstractTableModel>
#include <utility>
#include <vector>
#include <map>
#include "../Domain/Masina.h"
#include <QBrush>

using namespace std;

class TableModel : public QAbstractTableModel{
private:
//Q_OBJECT
    vector<Masina> masini;
public:
    TableModel(vector<Masina> ms) : masini(std::move(ms)){}

    QVariant data(const QModelIndex& index, int role = Qt::DisplayRole) const override;

    QVariant headerData(int section, Qt::Orientation orientation,int role) const override;

    int rowCount(const QModelIndex& parent = QModelIndex()) const override;

    int columnCount(const QModelIndex& parent = QModelIndex()) const override;

    void setMasini(vector<Masina> cars);

};


#endif //MASINI_TABELMODEL_H
