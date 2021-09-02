/*
 *  ARWrapperNativeCarsExample.cpp
 *  ARToolKit for Android
 *
 *  Disclaimer: IMPORTANT:  This Daqri software is supplied to you by Daqri
 *  LLC ("Daqri") in consideration of your agreement to the following
 *  terms, and your use, installation, modification or redistribution of
 *  this Daqri software constitutes acceptance of these terms.  If you do
 *  not agree with these terms, please do not use, install, modify or
 *  redistribute this Daqri software.
 *
 *  In consideration of your agreement to abide by the following terms, and
 *  subject to these terms, Daqri grants you a personal, non-exclusive
 *  license, under Daqri's copyrights in this original Daqri software (the
 *  "Daqri Software"), to use, reproduce, modify and redistribute the Daqri
 *  Software, with or without modifications, in source and/or binary forms;
 *  provided that if you redistribute the Daqri Software in its entirety and
 *  without modifications, you must retain this notice and the following
 *  text and disclaimers in all such redistributions of the Daqri Software.
 *  Neither the name, trademarks, service marks or logos of Daqri LLC may
 *  be used to endorse or promote products derived from the Daqri Software
 *  without specific prior written permission from Daqri.  Except as
 *  expressly stated in this notice, no other rights or licenses, express or
 *  implied, are granted by Daqri herein, including but not limited to any
 *  patent rights that may be infringed by your derivative works or by other
 *  works in which the Daqri Software may be incorporated.
 *
 *  The Daqri Software is provided by Daqri on an "AS IS" basis.  DAQRI
 *  MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION
 *  THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS
 *  FOR A PARTICULAR PURPOSE, REGARDING THE DAQRI SOFTWARE OR ITS USE AND
 *  OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.
 *
 *  IN NO EVENT SHALL DAQRI BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL
 *  OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 *  SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 *  INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION,
 *  MODIFICATION AND/OR DISTRIBUTION OF THE DAQRI SOFTWARE, HOWEVER CAUSED
 *  AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE),
 *  STRICT LIABILITY OR OTHERWISE, EVEN IF DAQRI HAS BEEN ADVISED OF THE
 *  POSSIBILITY OF SUCH DAMAGE.
 *
 *  Copyright 2015 Daqri LLC. All Rights Reserved.
 *  Copyright 2011-2015 ARToolworks, Inc. All Rights Reserved.
 *
 *  Author(s): Julian Looser, Philip Lamb
 *  Alterado por: Letícia Porto Soares. 2018.
 */

#include <AR/gsub_es.h>
#include <Eden/glm.h>
#include <jni.h>
#include <ARWrapper/ARToolKitWrapperExportedAPI.h>
#include <unistd.h> // chdir()
#include <android/log.h>

// Diretiva do caminho da classe Java NativeRenderer que possui os métodos nativos
#define JNIFUNCTION_DEMO(sig) Java_org_artoolkit_ar_samples_QuiRA_ar_NativeRenderer_##sig

//Métodos nativos
extern "C" {

JNIEXPORT void JNICALL JNIFUNCTION_DEMO(demoInitialise(JNIEnv * env, jobject object));

JNIEXPORT void JNICALL JNIFUNCTION_DEMO(demoSurfaceCreated(JNIEnv * env, jobject object));

JNIEXPORT void JNICALL JNIFUNCTION_DEMO(demoDrawFrame(JNIEnv * env, jobject obj));

JNIEXPORT void JNICALL JNIFUNCTION_DEMO(demoSurfaceChanged(JNIEnv * env, jobject object, jint w, jint h));
JNIEXPORT void JNICALL JNIFUNCTION_DEMO(demoShutdown(JNIEnv * env, jobject object));

//Métodos acrescentados
//Método que recebe os caminhos dos arquivos .obj
JNIEXPORT void JNICALL JNIFUNCTION_DEMO(caminhoObj(JNIEnv * env, jobject obj, jobjectArray objeto));

//Método que recebe os caminhos dos arquivos .patt
JNIEXPORT void JNICALL JNIFUNCTION_DEMO(caminhoPatt(JNIEnv * env, jobject obj, jobjectArray patt));

//Método que recebe a quantidade de Objetos
JNIEXPORT void JNICALL JNIFUNCTION_DEMO(quantidadeObjetos(JNIEnv * env, jobject obj, jint qtd));

};

//Estrutura que define o formato do Modelo (Objeto/Marcador)
typedef struct ARModel {
    int patternID;
    ARdouble transformationMatrix[16];
    bool visible;
    GLMmodel *obj;
} ARModel;

static float lightAmbient[4] = {0.1f, 0.1f, 0.1f, 1.0f};
static float lightDiffuse[4] = {1.0f, 1.0f, 1.0f, 1.0f};
static float lightPosition[4] = {0.0f, 0.0f, 1.0f, 0.0f};

//Variável para receber número de modelos (Objeto/Marcador)
int nummodels;

//Implementação do método que recebe quantidade de modelos
JNIEXPORT void JNICALL JNIFUNCTION_DEMO (quantidadeObjetos(JNIEnv * env, jobject obj, jint qtd)){
        nummodels = (int)qtd;
}

#define NUM_MODELS 128 //Instância estática que define a quantidade máxima de modelos suportados (tentar deixar dinâmico)

static ARModel models[NUM_MODELS] = {NUM_MODELS}; //Matriz de estruturas de modelos

const  char  * model0file[NUM_MODELS]; //Matriz para receber caminhos dos arquivos .obj
const char * arqpatt[NUM_MODELS]; //Matriz para receber caminhos dos arquivos .patt


//Implementação do método que recebe os caminhos dos arquivos .obj
JNIEXPORT void JNICALL JNIFUNCTION_DEMO (caminhoObj(JNIEnv * env, jobject obj, jobjectArray objeto)){
        jstring string;
        const char * modellocal;
        int stringCount = env->GetArrayLength(objeto);
        for (int i=0; i<stringCount; i++) {
                string = (jstring) (env->GetObjectArrayElement(objeto, i));
                modellocal = env->GetStringUTFChars(string, 0);
                model0file[i] = modellocal;
                // Don't forget to call `ReleaseStringUTFChars` when you're done.
                //env -> ReleaseStringUTFChars (string,modellocal); -> Ocasiona erro
        }
}

//Implementação do método que recebe os caminhos dos arquivos .patt
JNIEXPORT void JNICALL JNIFUNCTION_DEMO (caminhoPatt(JNIEnv * env, jobject obj, jobjectArray patt)){
        int stringCount = env->GetArrayLength(patt);
        jstring string;
        const char * pattlocal;
        for (int i=0; i<stringCount; i++) {
            string = (jstring) (env->GetObjectArrayElement(patt, i));
            pattlocal = env->GetStringUTFChars(string, 0);
            arqpatt[i] = pattlocal;
            // Don't forget to call `ReleaseStringUTFChars` when you're done.
            //env -> ReleaseStringUTFChars (string,pattlocal); -> Ocasiona erro
        }
}

//Método que inicializa os objetos com arquivos .patt e .obj
JNIEXPORT void JNICALL JNIFUNCTION_DEMO(demoInitialise(JNIEnv * env, jobject object)) {
    //for com tamanho igual ao total de modelos recebidos do Banco de Dados
    for (int i=0; i<nummodels; i++){
            models[i].patternID = arwAddMarker(arqpatt[i]); //Indica o caminho do arquivo .patt
            arwSetMarkerOptionBool(models[i].patternID, ARW_MARKER_OPTION_SQUARE_USE_CONT_POSE_ESTIMATION, false);
            arwSetMarkerOptionBool(models[i].patternID, ARW_MARKER_OPTION_FILTERED, true);

            //Indica o caminho do arquivo .obj
            models[i].obj = glmReadOBJ2(model0file[i], 0, 0); // context 0, don't read textures yet.


            if (!models[i].obj) {
                LOGE("Error loading model from file '%s'.", model0file[i]);
                exit(-1);
            }
            glmScale(models[i].obj, 20.000f);
            //glmRotate(models[0].obj, 3.14159f / 2.0f, 1.0f, 0.0f, 0.0f);
            glmCreateArrays(models[i].obj, GLM_SMOOTH | GLM_MATERIAL | GLM_TEXTURE);
            models[i].visible = false;
    }
}

JNIEXPORT void JNICALL JNIFUNCTION_DEMO(demoShutdown(JNIEnv * env, jobject object)) {
}

JNIEXPORT void JNICALL JNIFUNCTION_DEMO(demoSurfaceCreated(JNIEnv * env, jobject object)) {
    glStateCacheFlush(); // Make sure we don't hold outdated OpenGL state.
    for (int i = 0; i < nummodels; i++) {
        if (models[i].obj) {
            glmDelete(models[i].obj, 0);
            models[i].obj = NULL;
        }
    }
}

JNIEXPORT void JNICALL JNIFUNCTION_DEMO(demoSurfaceChanged(JNIEnv * env, jobject object, jint w, jint h)) {
    // glViewport(0, 0, w, h) has already been set.
}

JNIEXPORT void JNICALL JNIFUNCTION_DEMO(demoDrawFrame(JNIEnv * env, jobject obj)) {
    glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    // Set the projection matrix to that provided by ARToolKit.
    float proj[16];
    arwGetProjectionMatrix(proj);
    glMatrixMode(GL_PROJECTION);
    glLoadMatrixf(proj);
    glMatrixMode(GL_MODELVIEW);

    glStateCacheEnableDepthTest();

    glStateCacheEnableLighting();

    glEnable(GL_LIGHT0);

    //"Desenhando" os objetos
    for (int i = 0; i < nummodels; i++) {
        models[i].visible = arwQueryMarkerTransformation(models[i].patternID, models[i].transformationMatrix);

        if (models[i].visible) {
            glLoadMatrixf(models[i].transformationMatrix);

            glLightfv(GL_LIGHT0, GL_AMBIENT, lightAmbient);
            glLightfv(GL_LIGHT0, GL_DIFFUSE, lightDiffuse);
            glLightfv(GL_LIGHT0, GL_POSITION, lightPosition);

            glmDrawArrays(models[i].obj, 0);
        }

    }

}
