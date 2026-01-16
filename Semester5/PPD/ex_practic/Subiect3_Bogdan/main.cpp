#include <mpi.h>
#include <iostream>
#include <vector>
#include <fstream>

using namespace std;

int proceseazaNumar(int& numar, int X) {
    int temp = abs(numar);
    int sum = 0;

    while (temp > 0) {
        sum += temp % 10;
        temp /= 10;
    }

    if (sum < X) {
        numar = numar * 2;
        return 1;
    }
    numar = numar / 2;
    return 2;
}

int main(int argc, char** argv) {
    MPI_Init(&argc, &argv);

    int world_size, world_rank;
    MPI_Comm_size(MPI_COMM_WORLD, &world_size);
    MPI_Comm_rank(MPI_COMM_WORLD, &world_rank);

    int X = 0;
    int N = 0; 
    vector<vector<int>> payloads; 
    vector<int> my_data; 

    if (world_rank == 0) {
        ifstream fin("numbers.txt");
        if (!fin.is_open()) {
            cerr << "[Eroare] Nu s-a gasit fisierul numbers.txt!" << endl;
            MPI_Abort(MPI_COMM_WORLD, 1);
        }

        fin >> X;

        vector<int> all_numbers;
        int val;
        while (fin >> val) {
            all_numbers.push_back(val);
        }
        fin.close();

        N = all_numbers.size();
        payloads.resize(world_size);

        vector<int> even_procs;
        vector<int> odd_procs;
        for(int p = 0; p < world_size; ++p) {
            if(p % 2 == 0) even_procs.push_back(p);
            else odd_procs.push_back(p);
        }

        int idx_even = 0;
        int idx_odd = 0;

        for (int i = 0; i < N; i++) {
            if (i % 2 == 0) {
                int target = even_procs[idx_even % even_procs.size()];
                payloads[target].push_back(all_numbers[i]);
                idx_even++;
            } else {
                if (!odd_procs.empty()) {
                    int target = odd_procs[idx_odd % odd_procs.size()];
                    payloads[target].push_back(all_numbers[i]);
                    idx_odd++;
                } else {
                    payloads[0].push_back(all_numbers[i]);
                }
            }
        }

        cout << "[Rank 0] X citit: " << X << ". Numar total elemente: " << N << endl;

        MPI_Bcast(&X, 1, MPI_INT, 0, MPI_COMM_WORLD);

        for (int p = 1; p < world_size; p++) {
            int count = payloads[p].size();
            MPI_Send(&count, 1, MPI_INT, p, 0, MPI_COMM_WORLD);
            if (count > 0) {
                MPI_Send(payloads[p].data(), count, MPI_INT, p, 1, MPI_COMM_WORLD);
            }
        }

        my_data = payloads[0];

    } else {
        MPI_Bcast(&X, 1, MPI_INT, 0, MPI_COMM_WORLD);

        int count = 0;
        MPI_Recv(&count, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

        my_data.resize(count);
        if (count > 0) {
            MPI_Recv(my_data.data(), count, MPI_INT, 0, 1, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
        }
    }

    int local_A = 0;
    int local_B = 0;

    for (size_t i = 0; i < my_data.size(); i++) {
        int res = proceseazaNumar(my_data[i], X);
        if (res == 1) local_A++;
        else local_B++;
    }

    if (world_rank == 0) {
        payloads[0] = my_data;
        int total_A = local_A;
        int total_B = local_B;

        for (int p = 1; p < world_size; p++) {
            int count = payloads[p].size();

            if (count > 0) {
                MPI_Recv(payloads[p].data(), count, MPI_INT, p, 2, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
            }

            int recv_A, recv_B;
            MPI_Recv(&recv_A, 1, MPI_INT, p, 3, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
            MPI_Recv(&recv_B, 1, MPI_INT, p, 4, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

            total_A += recv_A;
            total_B += recv_B;
        }

        vector<int> final_result;
        final_result.reserve(N);

        vector<int> even_procs;
        vector<int> odd_procs;
        for(int p = 0; p < world_size; ++p) {
            if(p % 2 == 0) even_procs.push_back(p);
            else odd_procs.push_back(p);
        }

        vector<int> current_idx(world_size, 0); // La ce index suntem in bufferul fiecarui proces
        int idx_even = 0;
        int idx_odd = 0;

        for (int i = 0; i < N; i++) {
            int sursa = 0;
            if (i % 2 == 0) {
                sursa = even_procs[idx_even % even_procs.size()];
                idx_even++;
            } else {
                if (!odd_procs.empty()) {
                    sursa = odd_procs[idx_odd % odd_procs.size()];
                    idx_odd++;
                } else {
                    sursa = 0;
                }
            }

            final_result.push_back(payloads[sursa][current_idx[sursa]]);
            current_idx[sursa]++;
        }

        ofstream fout("result.txt");
        for (int val : final_result) {
            fout << val << "\n";
        }
        fout.close();

        cout << "[Final] Total A (inmultite): " << total_A << endl;
        cout << "[Final] Total B (impartite): " << total_B << endl;
        cout << "[Final] Rezultatele au fost scrise in result.txt" << endl;

    } else {
        int count = my_data.size();
        if (count > 0) {
            MPI_Send(my_data.data(), count, MPI_INT, 0, 2, MPI_COMM_WORLD);
        }
        MPI_Send(&local_A, 1, MPI_INT, 0, 3, MPI_COMM_WORLD);
        MPI_Send(&local_B, 1, MPI_INT, 0, 4, MPI_COMM_WORLD);
    }

    MPI_Finalize();
    return 0;
}