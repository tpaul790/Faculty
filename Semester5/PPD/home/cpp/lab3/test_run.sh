#!/bin/bash

if [ "$#" -lt 5 ]; then
    echo "Usage: $0 <method> <num_processes> <input_file_name1> <input_file_name2> <output_file_name>"
    exit 1
fi

BASE_PATH="./cmake-build-debug"
SEQ_PATH="$BASE_PATH/sequential"
MPI_SEND_RECV_PRECOMPUTE_PATH="$BASE_PATH/mpi_send_recv_precomputation"
MPI_SEND_RECV_POSTCOMPUTE_PATH="$BASE_PATH/mpi_send_recv_postcompute"
MPI_SCATTER_GATHER_PATH="$BASE_PATH/mpi_scatter_gather"
MPI_NON_BLOCKING_PATH="$BASE_PATH/mpi_isend_irecv"

METHOD=$1
NUM_PROCESSES=$2
INPUT_FILE1=$3
INPUT_FILE2=$4
OUTPUT_FILE=$5

RUNS=10
RESULTS_CSV="results/execution_results.csv"
if [ ! -f "$RESULTS_CSV" ]; then
      echo "N1 Size, N2 Size, No. Processes, Output File, Average Execution Time (ms)" > "$RESULTS_CSV"
fi

total_time=0

if [ "$METHOD" == "SEQUENTIAL" ]; then
    RUN_EXEC="$SEQ_PATH $INPUT_FILE1 $INPUT_FILE2 $OUTPUT_FILE"
elif [ "$METHOD" == "MPI_SEND_RECV_PRECOMPUTE" ]; then
    RUN_EXEC="mpirun --oversubscribe -np $NUM_PROCESSES $MPI_SEND_RECV_PRECOMPUTE_PATH $INPUT_FILE1 $INPUT_FILE2 $OUTPUT_FILE"
elif [ "$METHOD" == "MPI_SEND_RECV_POSTCOMPUTE" ]; then
    RUN_EXEC="mpirun --oversubscribe -np $NUM_PROCESSES $MPI_SEND_RECV_POSTCOMPUTE_PATH $INPUT_FILE1 $INPUT_FILE2 $OUTPUT_FILE"
elif [ "$METHOD" == "MPI_SCATTER_GATHER" ]; then
    RUN_EXEC="mpirun --oversubscribe -np $NUM_PROCESSES $MPI_SCATTER_GATHER_PATH $INPUT_FILE1 $INPUT_FILE2 $OUTPUT_FILE"
elif [ "$METHOD" == "MPI_ISEND_IRECV" ]; then
    RUN_EXEC="mpirun --oversubscribe -np $NUM_PROCESSES $MPI_NON_BLOCKING_PATH $INPUT_FILE1 $INPUT_FILE2 $OUTPUT_FILE"
else
    echo "Invalid method: $METHOD"
    exit 1
fi

for (( run=1; run<=RUNS; run++ )); do
    exec_time=$($RUN_EXEC)
    if [ $? -ne 0 ]; then
        echo "Execution failed for $METHOD method. Output is invalid. Skipping further tests."
        continue
    fi
    total_time=$(echo "$total_time + $exec_time" | bc)
#    echo "Execution $run: Time = $exec_time ms"
done

average_time=$(echo "scale=2; $total_time / $RUNS" | bc)
output_file="${OUTPUT_FILE}_${METHOD}.txt"

echo "' ', ' ', $NUM_PROCESSES, $output_file, $average_time" >> "$RESULTS_CSV"
echo "Executed $METHOD with $NUM_PROCESSES processes: Average Time = $average_time ms"

