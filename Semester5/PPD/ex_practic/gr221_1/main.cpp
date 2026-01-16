#include <mpi.h>
#include <iostream>
#include <fstream>
#include <vector>

#define TAG_TERMINATE 999999

int main(int argc, char** argv) {
    MPI_Init(&argc, &argv);

    int rank, size;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &size);

    if (size < 2) {
        if (rank == 0) std::cerr << "Eroare: Minim 2 procese necesare." << std::endl;
        MPI_Finalize();
        return 1;
    }

    int N;

    if (rank == 0) {
        std::ifstream fin("input.txt");
        if (!fin.is_open()) {
            std::cerr << "Eroare deschidere fisier." << std::endl;
            MPI_Abort(MPI_COMM_WORLD, 1);
        }

        fin >> N;
        MPI_Bcast(&N, 1, MPI_INT, 0, MPI_COMM_WORLD);

        std::vector<int> matrix(N * N);
        std::vector<int> final_results(N);

        std::vector<MPI_Request> send_reqs(N);
        std::vector<MPI_Request> recv_reqs(N);

        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                fin >> matrix[i * N + j];
            }

            int dest_worker = i % (size - 1) + 1;

            MPI_Irecv(&final_results[i], 1, MPI_INT, dest_worker, i, MPI_COMM_WORLD, &recv_reqs[i]);

            MPI_Isend(&matrix[i * N], N, MPI_INT, dest_worker, i, MPI_COMM_WORLD, &send_reqs[i]);
        }
        fin.close();

        MPI_Waitall(N, recv_reqs.data(), MPI_STATUSES_IGNORE);

        MPI_Waitall(N, send_reqs.data(), MPI_STATUSES_IGNORE);

        for (int k = 1; k < size; ++k) {
            MPI_Send(NULL, 0, MPI_INT, k, TAG_TERMINATE, MPI_COMM_WORLD);
        }

        std::ofstream fout("output.txt");
        for (int i = 0; i < N; ++i) {
            fout << final_results[i] << std::endl;
        }
        fout.close();
    }
    else {
        MPI_Bcast(&N, 1, MPI_INT, 0, MPI_COMM_WORLD);
        std::vector<int> row_buffer(N);
        MPI_Status status;

        while (true) {
            MPI_Recv(row_buffer.data(), N, MPI_INT, 0, MPI_ANY_TAG, MPI_COMM_WORLD, &status);

            if (status.MPI_TAG == TAG_TERMINATE) break;

            int row_sum = 0;
            for(int val : row_buffer) row_sum += val;

            int result = row_sum;
            MPI_Send(&result, 1, MPI_INT, 0, status.MPI_TAG, MPI_COMM_WORLD);
        }
    }

    MPI_Finalize();
    return 0;
}