#include <mpi.h>
#include <iostream>
#include <vector>
#include <fstream>

using namespace std;

int aplica_h(int numar, int X) {
    if (numar < X) {
        return numar * X;
    }
    return numar / X;
}

const int PROD_TAG = 0;
const int ARR_TAG = 1;

int main(int argc, char** argv) {
    MPI_Init(&argc, &argv);

    int rank, size;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &size);

    if ( size < 2 ) {
        cerr << "Avem nevoie de minim 2 procese!" << endl;
        MPI_Abort(MPI_COMM_WORLD, 1);
    }

    int X, N;
    vector<int> numbers;
    vector<int> local_numbers;
    int elements_per_proc;

    if (rank == 0) {
        ifstream fin("numbers.txt");

        fin >> X;
        fin >> N;

        int temp_val;
        for (int i = 0; i < N; i++) {
            fin >> temp_val;
            numbers.push_back(temp_val);
        }
        fin.close();

        elements_per_proc = numbers.size() / size;
    }

    MPI_Bcast(&X, 1, MPI_INT, 0, MPI_COMM_WORLD);
    MPI_Bcast(&elements_per_proc, 1, MPI_INT, 0, MPI_COMM_WORLD);

    local_numbers.resize(elements_per_proc);

    MPI_Scatter(numbers.data(), elements_per_proc, MPI_INT,
                local_numbers.data(), elements_per_proc, MPI_INT,
                0, MPI_COMM_WORLD);

    int local_prod = 1;
    
    for (int i = 0; i < elements_per_proc; i++) {
        local_prod *= local_numbers[i];
        local_numbers[i] = aplica_h(local_numbers[i], X);
    }

    int received_prod = 1;
    int current_cumulative = local_prod;

    if (rank == 0) {
        MPI_Send(&local_prod, 1, MPI_INT, 1, PROD_TAG, MPI_COMM_WORLD);
        
        int final_global_prod;
        MPI_Recv(&final_global_prod, 1, MPI_INT, size - 1, PROD_TAG, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
        
        cout << "Produsul cumulativ total primit de Rank 0: " << final_global_prod << endl;
    } else {
        MPI_Recv(&received_prod, 1, MPI_INT, rank - 1, PROD_TAG, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
        
        current_cumulative = received_prod * local_prod;
        
        int next_rank = (rank + 1) % size;
        MPI_Send(&current_cumulative, 1, MPI_INT, next_rank, PROD_TAG, MPI_COMM_WORLD);
    }

    if (rank != 0) {
        MPI_Send(local_numbers.data(), elements_per_proc, MPI_INT, 0, ARR_TAG, MPI_COMM_WORLD);
    } else {
        vector<int> final_results(N);
        
        for(int i=0; i < elements_per_proc; i++) {
            final_results[i] = local_numbers[i];
        }

        for (int p = 1; p < size; p++) {
            vector<int> buffer(elements_per_proc);
            MPI_Recv(buffer.data(), elements_per_proc, MPI_INT, p, ARR_TAG, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
            
            int start_index = p * elements_per_proc;
            for(int i=0; i<elements_per_proc; i++) {
                final_results[start_index + i] = buffer[i];
            }
        }

        ofstream fout("result.txt");
        for (int i = 0; i < N; i++) {
            fout << final_results[i] << endl;
        }
    }

    MPI_Finalize();
    return 0;
}