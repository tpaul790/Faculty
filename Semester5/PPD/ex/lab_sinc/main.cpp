#include <iostream>
#include <fstream>
#include <mpi.h>

using namespace std;

const int CHUNK = 100;
const int SUM_TAG = 0;
const int ARRAY_TAG = 1;

void init(vector<int>& global, int size) {
    srand(time(NULL));
    for (int i = 0; i < size; ++i) {
        global[i] = rand() % 100;
    }
}

int secv_sum(vector<int>& global, int size) {
    int sum = 0;
    for (int i = 0; i < size; ++i) {
        sum += global[i];
    }
    return sum;
}

int main(int argc, char** argv) {
    MPI_Init(&argc, &argv);

    int rank, size;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &size);
    int WORKERS = size - 1;
    const int GLOBAL_SIZE = CHUNK * WORKERS;
    vector<int> global(GLOBAL_SIZE);

    if (rank == 0) {
        int global_sum = 0;
        init(global, GLOBAL_SIZE);
        MPI_Send(global.data(), GLOBAL_SIZE, MPI_INT, 1, ARRAY_TAG, MPI_COMM_WORLD);

        int local_sum = 0;
        for (int i = 0; i < WORKERS; ++i) {
            MPI_Recv(&local_sum, 1, MPI_INT, MPI_ANY_SOURCE, SUM_TAG, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
            global_sum += local_sum;
        }

        cout << "Calculated sum: " << global_sum << endl;
        cout << "Expected sum: " << secv_sum(global, GLOBAL_SIZE) << endl;
    }else {
        int RECV_SIZE = GLOBAL_SIZE - CHUNK * (rank - 1);
        vector<int> local(RECV_SIZE);
        MPI_Recv(local.data(), RECV_SIZE, MPI_INT, rank-1, ARRAY_TAG, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

        int local_sum = 0;
        for (int i = 0; i < CHUNK; ++i) {
            local_sum += local[i];
        }

        MPI_Send(&local_sum, 1, MPI_INT, 0, SUM_TAG, MPI_COMM_WORLD);

        if (rank < WORKERS) {
            const int SEND_SIZE = GLOBAL_SIZE - CHUNK * rank;
            MPI_Send(local.data() + CHUNK, SEND_SIZE, MPI_INT, rank+1, ARRAY_TAG, MPI_COMM_WORLD);
        }
    }

    MPI_Finalize();
    return 0;
}

// int main() {
//     ofstream fout1("n1.txt");
//     ofstream fout2("n2.txt");
//     ofstream fout("result.txt");
//
//     generate_file_data(fout1, N1);
//     generate_file_data(fout2, N2);
//     return 0;
// }