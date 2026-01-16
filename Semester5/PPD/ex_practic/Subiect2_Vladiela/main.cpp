#include <mpi.h>
#include <iostream>
#include <vector>
#include <fstream>

using namespace std;

int main(int argc, char** argv) {
    MPI_Init(&argc, &argv);

    int world_size, world_rank;
    MPI_Comm_size(MPI_COMM_WORLD, &world_size);
    MPI_Comm_rank(MPI_COMM_WORLD, &world_rank);

    if (world_size < 2) {
        if (world_rank == 0) {
            cerr << "Eroare: Programul necesita cel putin 2 procese (mpirun -np 2 ...)" << endl;
        }
        MPI_Finalize();
        return 0;
    }

    int N = 0;

    // --- ETAPA 1: Citirea N si Broadcast ---
    if (world_rank == 0) {
        ifstream fin("input.txt");
        if (!fin.is_open()) {
            cerr << "Eroare: Nu s-a putut deschide input.txt" << endl;
            MPI_Abort(MPI_COMM_WORLD, 1);
        }
        fin >> N;
        fin.close();
    }

    MPI_Bcast(&N, 1, MPI_INT, 0, MPI_COMM_WORLD);

    if (world_rank == 0) {
        ifstream fin("input.txt");
        int tempN; fin >> tempN; // Sarim peste N-ul citit deja

        vector<int> row_buffer(N);
        int num_workers = world_size - 1;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                fin >> row_buffer[j];
            }

            int dest_rank = (i % num_workers) + 1;
            
            MPI_Send(row_buffer.data(), N, MPI_INT, dest_rank, 0, MPI_COMM_WORLD);
        }
        fin.close();

        vector<vector<int>> results_collection(world_size);

        for (int p = 1; p < world_size; p++) {
            int count;
            MPI_Recv(&count, 1, MPI_INT, p, 5, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
            
            results_collection[p].resize(count);
            MPI_Recv(results_collection[p].data(), count, MPI_INT, p, 1, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
        }

        ofstream fout("output.txt");
        
        vector<int> current_idx(world_size, 0);

        for (int i = 0; i < N; i++) {
            int source_worker = (i % num_workers) + 1;
            
            int sum_val = results_collection[source_worker][current_idx[source_worker]];
            
            fout << sum_val << "\n";
            
            current_idx[source_worker]++;
        }
        fout.close();
        cout << "[Rank 0] Procesare completa. Rezultate scrise in output.txt" << endl;

    } else {
        int num_workers = world_size - 1;
        
        int my_rows_count = 0;
        for (int i = 0; i < N; i++) {
            if (i % num_workers + 1 == world_rank) {
                my_rows_count++;
            }
        }

        vector<int> row_buffer(N);
        vector<int> my_results;
        my_results.reserve(my_rows_count);

        for (int k = 0; k < my_rows_count; k++) {
            MPI_Recv(row_buffer.data(), N, MPI_INT, 0, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

            int line_sum = 0;
            for (int val : row_buffer) {
                line_sum += val;
            }
            my_results.push_back(line_sum);
        }
        int count = my_results.size();
        MPI_Send(&count, 1, MPI_INT, 0, 5, MPI_COMM_WORLD);
        MPI_Send(my_results.data(), my_rows_count, MPI_INT, 0, 1, MPI_COMM_WORLD);
    }

    MPI_Finalize();
    return 0;
}