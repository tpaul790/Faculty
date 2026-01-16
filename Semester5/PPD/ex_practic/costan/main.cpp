#include <mpi.h>
#include <iostream>
#include <vector>
#include <fstream>

using namespace std;

int getSumDigits(int n) {
    int sum = 0;
    n = abs(n); 
    while (n > 0) {
        sum += n % 10;
        n /= 10;
    }
    return sum;
}

int main(int argc, char** argv) {
    MPI_Init(&argc, &argv);

    int size, rank;
    MPI_Comm_size(MPI_COMM_WORLD, &size);
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);

    int N = 0;
    int X = 0;
    vector<int> original_data;
    vector<int> send_buffer;
    
    if (rank == 0) {
        ifstream fin("numbers.txt");
        if (!fin.is_open()) {
            cerr << "Eroare: Nu s-a putut deschide numbers.txt" << endl;
            MPI_Abort(MPI_COMM_WORLD, 1);
        }
        
        fin >> N >> X;
        original_data.resize(N);
        for (int i = 0; i < N; i++) {
            fin >> original_data[i];
        }
        fin.close();

        if (N % size != 0) {
            cerr << "Eroare: N trebuie sa fie divizibil cu P pentru distributie egala." << endl;
            MPI_Abort(MPI_COMM_WORLD, 1);
        }

        send_buffer.resize(N);
        int chunk_size = N / size;
        
        int current_even_idx = 0;
        int current_odd_idx = 1;

        for (int r = 0; r < size; r++) {
            for (int i = 0; i < chunk_size; i++) {
                int buffer_pos = r * chunk_size + i;

                if (r % 2 == 0) {
                    send_buffer[buffer_pos] = original_data[current_even_idx];
                    current_even_idx += 2;
                } else {
                    send_buffer[buffer_pos] = original_data[current_odd_idx];
                    current_odd_idx += 2;
                }
            }
        }
    }

    MPI_Bcast(&X, 1, MPI_INT, 0, MPI_COMM_WORLD);
    MPI_Bcast(&N, 1, MPI_INT, 0, MPI_COMM_WORLD);

    int chunk_size = N / size;
    vector<int> local_data(chunk_size);

    MPI_Scatter(send_buffer.data(), chunk_size, MPI_INT, 
                local_data.data(), chunk_size, MPI_INT, 
                0, MPI_COMM_WORLD);

    int count_A = 0;
    int count_B = 0;

    for (int i = 0; i < chunk_size; i++) {
        int s = getSumDigits(local_data[i]);
        if (s < X) {
            local_data[i] = local_data[i] * 2;
            count_A++;
        } else {
            local_data[i] = local_data[i] / 2;
            count_B++;
        }
    }

    vector<int> gather_buffer;
    if (rank == 0) {
        gather_buffer.resize(N);
    }

    MPI_Gather(local_data.data(), chunk_size, MPI_INT,
               gather_buffer.data(), chunk_size, MPI_INT,
               0, MPI_COMM_WORLD);

    if (rank == 0) {
        vector<int> final_result(N);
        int current_even_idx = 0;
        int current_odd_idx = 1;

        for (int r = 0; r < size; r++) {
            for (int i = 0; i < chunk_size; i++) {
                int buffer_pos = r * chunk_size + i;
                
                if (r % 2 == 0) {
                    final_result[current_even_idx] = gather_buffer[buffer_pos];
                    current_even_idx += 2;
                } else {
                    final_result[current_odd_idx] = gather_buffer[buffer_pos];
                    current_odd_idx += 2;
                }
            }
        }

        ofstream fout("result.txt");
        for (int i = 0; i < N; i++) {
            fout << final_result[i] << " ";
        }
        fout.close();
        cout << "[Rank 0] Vectorul modificat a fost scris in result.txt" << endl;
    }

    int total_A = 0;
    int total_B = 0;

    MPI_Reduce(&count_A, &total_A, 1, MPI_INT, MPI_SUM, 1, MPI_COMM_WORLD);
    MPI_Reduce(&count_B, &total_B, 1, MPI_INT, MPI_SUM, 1, MPI_COMM_WORLD);

    if (rank == 1) {
        cout << "--- Statistici (Calculate de Rank 1) ---" << endl;
        cout << "Total numere modificate conform Cazul 1 (inmultire): " << total_A << endl;
        cout << "Total numere modificate conform Cazul 2 (impartire): " << total_B << endl;
    }

    MPI_Finalize();
    return 0;
}