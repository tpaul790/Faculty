#include <iostream>
#include <mpi.h>
#include <fstream>
#include <vector>
#include <cmath>
#include <algorithm>

using namespace std;

const int SIZE_TAG = 0;
const int DATA_TAG = 1;
const int SUM_TAG = 2;

void read_and_store(std::string filename, vector<double>& data, int& number) {
    ifstream fin(filename);

    if (!fin.is_open()) {
        std::cerr << "Error: Could not open file: " << filename << std::endl;
        return;
    }

    fin >> number;
    int size;
    fin >> size;
    data.resize(size);
    for (int i = 0; i < size; i++) {
        fin >> data[i];
    }
}

int main(int arc, char** argv) {
    MPI_Init(&arc, &argv);

    int rank, size;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &size);

    if (size % 2 != 0 || size <= 2) {
        if (rank == 0) {
            cerr << "Error: The number of processes (M) must be even (2*N) and greater than 2." << endl;
            cerr << "Current size is " << size << ". Please run with a suitable number of processes (e.g., 4, 6, 8...)." << endl;
        }
        MPI_Finalize();
        return 1;
    }

    int color = rank % 2;
    MPI_Comm sub_comm;
    MPI_Comm_split(MPI_COMM_WORLD, color, rank, &sub_comm);

    int sub_rank, sub_size;
    MPI_Comm_rank(sub_comm, &sub_rank);
    MPI_Comm_size(sub_comm, &sub_size);

    if (color == 0) {
        vector<double> dateA;
        int a = 0;
        int send_size = 0;

        if (rank == 0) {
            read_and_store("inputA", dateA, a);
            send_size = static_cast<int>(dateA.size() / sub_size);
        }

        MPI_Bcast(&a, 1, MPI_INT, 0, sub_comm);
        MPI_Bcast(&send_size, 1, MPI_INT, 0, sub_comm);

        vector<double> segmentA(send_size);

        MPI_Scatter(dateA.data(), send_size, MPI_DOUBLE,
                    segmentA.data(), send_size, MPI_DOUBLE,
                    0, sub_comm);

        for (int i = 0; i < send_size; i++) {
            segmentA[i] = pow(segmentA[i], a);
        }

        int recv_size;
        MPI_Recv(&recv_size, 1, MPI_INT, rank + 1, SIZE_TAG, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
        vector<double> segmentB(recv_size);
        MPI_Recv(segmentB.data(), recv_size, MPI_DOUBLE, rank + 1, DATA_TAG, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

        double partial_sum = 0;

        for (int i = 0; i < send_size; i++) {
            partial_sum += segmentA[i] * segmentB[i];
        }


        if (rank == 0) {
            double total_sum = partial_sum;
            for (int i = 1; i < sub_size; i++) {
                double recv_sum;
                MPI_Recv(&recv_sum, 1, MPI_DOUBLE, MPI_ANY_SOURCE, SUM_TAG, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
                total_sum += recv_sum;
            }

            cout << "\n Final Result:\n";
            cout << "-------------------------------------------------\n";
            cout << "[ Rank = " << rank << " ] Total Sum S = " << total_sum << endl;
            cout << "-------------------------------------------------\n";
        } else {
            MPI_Send(&partial_sum, 1, MPI_DOUBLE, 0, SUM_TAG, MPI_COMM_WORLD);
        }

    }
    else {
        vector<double> dateB;
        int b = 0;
        int send_size = 0;

        if (sub_rank == 0) {
            read_and_store("inputB", dateB, b);
            send_size = static_cast<int>(dateB.size() / sub_size);
        }

        MPI_Bcast(&b, 1, MPI_INT, 0, sub_comm);
        MPI_Bcast(&send_size, 1, MPI_INT, 0, sub_comm);

        vector<double> segmentB(send_size);

        // Root 0 este Global Rank 1
        MPI_Scatter(dateB.data(), send_size, MPI_DOUBLE,
                    segmentB.data(), send_size, MPI_DOUBLE,
                    0, sub_comm);

        for (int i = 0; i < send_size; i++) {
            segmentB[i] = pow(segmentB[i], b);
        }

        MPI_Send(&send_size, 1, MPI_INT, rank - 1, SIZE_TAG, MPI_COMM_WORLD);
        MPI_Send(segmentB.data(), send_size, MPI_DOUBLE, rank - 1, DATA_TAG, MPI_COMM_WORLD);
    }

    MPI_Comm_free(&sub_comm);

    MPI_Finalize();
    return 0;
}