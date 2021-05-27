! Lecture3/loops1.f90

program loops1

   implicit none
   integer, parameter :: n = 10000
   real (kind=8), dimension(n) :: x, y   
   integer :: i

   do i=1,n
      x(i) = 3.d0 * i
      enddo

   do i=1,n
      y(i) = 2.d0 * x(i)
      enddo
   
   print *, "Last y computed: ", y(n)

   do i=1,3           ! prints 1,2,3
      print *, i
      enddo

   do i=5,11,2        ! prints 5,7,9,11
      print *, i
      enddo

   do i=6,2,-1        ! prints 6,5,4,3,2
      print *, i
      enddo

   i = 0
   do while (i < 5)   ! prints 0,1,2,3,4
      print *, i
      i = i+1
      enddo

end program loops1

