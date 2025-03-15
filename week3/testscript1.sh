order=1024
while [ $order -le 2048 ]
do
n_th=1 
while [ $n_th -le 4 ]
do
 iter=1 
	while [ $iter -le 1 ]
  do
      		java MyMatrixOP $n_th $order $order $order $order 2
            ((iter=iter+1))
        done
        ((n_th=n_th+n_th))
done
((order=order*2))
done


# order=1024
# while [ $order -le 2048 ]
# do
# n_th=1 
# while [ $n_th -le 4 ]
# do
#  iter=1 
# 	while [ $iter -le 1 ]
#   do
#       		java MyMatrixOP $n_th $order $order $order $order 2
#             ((iter=iter+1))
#         done
#         ((n_th=n_th+n_th))
# done
# ((order=order*2))
# done
