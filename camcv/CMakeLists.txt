cmake_minimum_required(VERSION 2.8)
project( camcv7 )
SET(COMPILE_DEFINITIONS -Werror)

#OPENCV
find_package( OpenCV REQUIRED )

#except if you’re pierre, change the folder where you installed libfacerec
#optional, only if you want to go till step 6 : face recognition
link_directories( /home/pi/bytefish-libfacerec-e1b143d ) 


include_directories(/opt/vc/userland-master/host_applications/linux/libs/bcm_host/include)
include_directories(/opt/vc/userland-master/interface/vcos)
include_directories(/opt/vc/userland-master)
include_directories(/opt/vc/userland-master/interface/vcos/pthreads)
include_directories(/opt/vc/userland-master/interface/vmcs_host/linux)
include_directories(/opt/vc/userland-master/interface/khronos/include)
include_directories(/opt/vc/userland-master/interface/khronos/common)
add_executable(camcv7 RaspiCamControl.c RaspiCLI.c RaspiPreview.c camcv7.cpp RaspiTex.c RaspiTexUtil.c gl_scenes/teapot.c gl_scenes/models.c gl_scenes/square.c gl_scenes/mirror.c gl_scenes/yuv.c gl_scenes/sobel.c tga.c)
target_link_libraries(camcv7 /opt/vc/lib/libmmal_core.so /opt/vc/lib/libmmal_util.so /opt/vc/lib/libmmal_vc_client.so /opt/vc/lib/libvcos.so /opt/vc/lib/libbcm_host.so /opt/vc/lib/libGLESv2.so /opt/vc/lib/libEGL.so /home/pi/bytefish-libfacerec-e1b143d/libopencv_facerec.a /usr/local/lib/libbcm2835.a   ${OpenCV_LIBS})


#####




