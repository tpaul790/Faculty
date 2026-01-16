#!/bin/bash

if [ "$#" -lt 2 ]; then
    echo "Usage: $0 <num_runs> <method> <type (if applicable)>"
    exit 1
fi

RUNS=$1
METHOD=$2
TYPE=${3:-""} # default to empty string if type is not provided
RESULTS_CSV="data/results/execution_results.csv"
JAVA_CP="../../../../build/classes/java/main"

if [ ! -f "$RESULTS_CSV" ]; then
      echo "N, M, K, Method, Type, Thread Count, Output File, Average Execution Time (ms)" > "$RESULTS_CSV"
fi

declare -a TEST_CASES=("10 10 3" "1000 1000 5" "10 10000 5" "10000 10 5" "10000 10000 5")
declare -a THREAD_COUNT=( "4" "2 4 8 16" "2 4 8 16" "2 4 8 16" "2 4 8 16")
declare -a PARALLEL_METHODS=("BATCH" "BLOCK" "DISTRIBUTION")
declare -a BATCH_TYPES=("ROWS" "COLUMNS")
declare -a DISTRIBUTION_TYPES=("LINEAR" "CYCLIC")

if [[ "$METHOD" != "SEQUENTIAL" && ! ${PARALLEL_METHODS[*]} =~ ${METHOD} ]]; then
  echo "Invalid method. Valid methods are: SEQUENTIAL, ${PARALLEL_METHODS[*]}"
  exit 1
fi

if [[ "$METHOD" == "BATCH" && ! ${BATCH_TYPES[*]} =~ ${TYPE} ]]; then
  echo "For BATCH method, valid types are: ${BATCH_TYPES[*]}"
  exit 1
elif [[ "$METHOD" == "DISTRIBUTION" && ! ${DISTRIBUTION_TYPES[*]} =~ ${TYPE} ]]; then
  echo "For DISTRIBUTION method, valid types are: ${DISTRIBUTION_TYPES[*]}"
  exit 1
elif [[ "$METHOD" == "BLOCK" && "$TYPE" != "" ]]; then
  echo "BLOCK method does not require a type. Do not pass a type argument."
  exit 1
fi

for i in "${!TEST_CASES[@]}"; do
  test_case=${TEST_CASES[$i]}
  threads=${THREAD_COUNT[$i]}

  read -r N M K <<< "$test_case"
  input_file="input_${N}_${M}_${K}.txt"

  if [ "$METHOD" == "SEQUENTIAL" ]; then
    exec_time=$(java -cp "$JAVA_CP" ppd.Main "$input_file" "0" "$METHOD")
    if [ $? -ne 0 ]; then
      echo "Execution failed for $input_file using $METHOD method. Output is invalid. Skipping further tests."
      continue
    fi

    output_file="output_${METHOD}_${N}_${M}_${K}.txt"
    echo "$N, $M, $K, $METHOD, None, 0, $output_file, $exec_time" >> "$RESULTS_CSV"
  else
    for thread_count in $threads; do
      total_time=0
      for (( run=1; run<=RUNS; run++ )); do
        exec_time=$(java -cp "$JAVA_CP" ppd.Main "$input_file" "$thread_count" "$METHOD" "$TYPE")
        if [ $? -ne 0 ]; then
          echo "Execution failed for $input_file using $METHOD method. Output is invalid. Skipping further tests."
          continue 2
        fi
        total_time=$(echo "$total_time + $exec_time" | bc)
      done

      average_time=$(echo "$total_time / $RUNS" | bc -l)
      output_file="output_${METHOD}_${TYPE}_${N}_${M}_${K}_${thread_count}.txt"
      echo "$N, $M, $K, $METHOD, $TYPE, $thread_count, $output_file, $average_time" >> "$RESULTS_CSV"
      echo "Executed $METHOD $TYPE for test case $test_case - $thread_count threads: Time = $exec_time ms"
    done
  fi
#  sort -t, -k1,1n -k2,2n -k3,3n -k8,8n "$RESULTS_CSV" -o "$RESULTS_CSV"
done
