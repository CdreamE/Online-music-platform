! Lecture3/arrreshp.f90

program arrreshp
      implicit none
      integer :: Afort(2,3),Afort1(3,2)
      integer*8::loc

      integer :: i,j

      Afort = reshape([10,20,30,40,50,60],[2,3])
      Afort1 = reshape([10,20,30,40,50,60],[3,2])

        
      do i=1,2
         print "('Afort(',i1,',*)=')", i
         do j=1,3
         print "('Afort(',i1,',',i1,')=',i2)", i, j, Afort(i,j)
         print *,'Addr=', loc(Afort(i,j))
         enddo
      enddo

      print *,''

       do i=1,3
         print "('Afort1(',i1,',*)=')", i
         do j=1,2
         print "('Afort1(',i1,',',i1,')=',i2)", i, j, Afort1(i,j)
         print *, 'Addr=',loc(Afort1(i,j))
         enddo
      enddo


end program arrreshp

