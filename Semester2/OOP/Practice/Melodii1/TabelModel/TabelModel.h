//
// Created by Titieni Paul on 23.06.2024.
//

#ifndef MELODII1_TABELMODEL_H
#define MELODII1_TABELMODEL_H


#include <QAbstractTableModel>
#include <utility>
#include <vector>
#include "../Domain/Melodie.h"

using namespace std;

class TableModel : public QAbstractTableModel{
private:
//Q_OBJECT
    vector<Melodie> melodii;
public:
    explicit TableModel(vector<Melodie> ms) : melodii(std::move(ms)){}

    QVariant data(const QModelIndex& index, int role = Qt::DisplayRole) const override;

    QVariant headerData(int section, Qt::Orientation orientation,int role) const override;

    int rowCount(const QModelIndex& parent = QModelIndex()) const override;

    int columnCount(const QModelIndex& parent = QModelIndex()) const override;

    void setProduse(vector<Melodie> melodii);

};


#endif //MELODII1_TABELMODEL_H
