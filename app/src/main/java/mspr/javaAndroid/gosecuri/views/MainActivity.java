package mspr.javaAndroid.gosecuri.views;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import mspr.javaAndroid.gosecuri.R;


import com.google.firebase.auth.FirebaseAuth;
import com.wonderkiln.camerakit.CameraKitEventListenerAdapter;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraView;
import java.util.List;

import dmax.dialog.SpotsDialog;
import mspr.javaAndroid.gosecuri.controller.Identity.CardIDExtractInfo;
import mspr.javaAndroid.gosecuri.controller.Identity.IdentityChecker;
import mspr.javaAndroid.gosecuri.controller.Identity.IdentityCheckerCallback;
import mspr.javaAndroid.gosecuri.model.Visitor;


public final class MainActivity extends AppCompatActivity {

	CameraView cameraView;


	//alert
	AlertDialog waitingDialog;
	AlertDialog camDialog;
	AlertDialog addDialog;
	AlertDialog userSearch;
	AlertDialog successDialog;
	AlertDialog failDialog;

	Button captureBtn;
	Context thisContext = this;

	@Override
	protected void onResume() {
		super.onResume();
        cameraView.start();
	}

	@Override
	protected void onPause() {
		super.onPause();
		cameraView.stop();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		FirebaseAuth mAuth = FirebaseAuth.getInstance();

		cameraView = (CameraView) findViewById(R.id.camera_view);
		captureBtn = findViewById(R.id.capture_btn);

		//quand on clique sur le bouton
		captureBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cameraView.start();
				cameraView.captureImage();
				waitingDialog = new SpotsDialog.Builder()
						.setCancelable(false)
						.setMessage("Analyse en cours")
						.setContext(thisContext)
						.build();
			}
		});

		//event camera view
		cameraView.addCameraKitListener(new CameraKitEventListenerAdapter() {

			@Override
			public void onImage(final CameraKitImage cameraKitImage) {

				cameraView.stop();
				waitingDialog.show();

				new Thread(new Runnable() {
					@Override
					public void run() {
						logic(cameraKitImage.getJpeg());
					}
				}).start();
			}
		});//fin camera listener
	}


	private void logic(byte[] image){


		//récupere les infos de la carte d'iddentité
		new CardIDExtractInfo(image).execute( new IdentityCheckerCallback() {

			//si un visiteur est trouvé a partir de des infos extraites
			@Override
			public void onVisitorFound(Visitor output) {
			    waitingDialog.dismiss();

				userSearch =new  SpotsDialog.Builder()
					.setCancelable(false)
					.setMessage("Recherche du Visiteur...")
					.setContext(MainActivity.this)
					.build();
				userSearch.show();

				final Visitor foundVisitor = output;
				final IdentityChecker checkIdentity = new IdentityChecker(output);


				//vérifie si l'utilisateur n'est pas déjà connu
				checkIdentity.execute(new IdentityCheckerCallback() {
					@Override
					public void onVisitorFound(Visitor output) {
						foundVisitor.UpdateVisitorVisite(new IdentityCheckerCallback() {
							@Override
							public void onSuccess(boolean output) {
								userSearch.dismiss();
								if(output){
									Succeed();
								}else{
									Failed();
								}
							}
						});
					}

					@Override
					public void onVisitorNotFound() {
						userSearch.dismiss();
						AddVisitor(foundVisitor);
					}
				});
			}

			@Override
			public void onListVisitorNotFound() {
				waitingDialog.dismiss();
				Failed();
			}
		}); //fin CardIDExtractInfo
	}

	private void AddVisitor(final Visitor foundVisitor){
		runOnUiThread(new Runnable() {
			public void run() {
				try {

					final View mView = getLayoutInflater().inflate(R.layout.activity_camera_view_dialog, null);
					Button mCapture = mView.findViewById(R.id.saveImgBtn);
					final CameraView mCamView = mView.findViewById(R.id.camera_view_dialog);
					mCamView.start();
					mCapture.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							addDialog =new  SpotsDialog.Builder()
									.setCancelable(false)
									.setMessage("Ajout d'un Nouveau Visiteur...")
									.setContext(mView.getContext())
									.build();

							mCamView.captureImage();
						}
					});

					mCamView.addCameraKitListener(new CameraKitEventListenerAdapter() {

						@Override
						public void onImage(CameraKitImage image) {
							mCamView.stop();

							addDialog.show();

							super.onImage(image);
							List<byte[]> visitorCurrentImg = foundVisitor.getImages();
							visitorCurrentImg.add(image.getJpeg());
							foundVisitor.setImages(visitorCurrentImg);

							foundVisitor.SaveVisitor(new IdentityCheckerCallback() {

								@Override
								public void onVisitorAdded() {
									addDialog.dismiss();
									camDialog.dismiss();
									Succeed();
								}

								@Override
								public void onVisitorNotAdded() {
									addDialog.dismiss();
									camDialog.dismiss();
									Failed();
								}
							});
						}
					});

					AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
					mBuilder.setView(mView);
					camDialog = mBuilder.create();
					camDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
					camDialog.setCancelable(false);
					camDialog.show();

				}catch (Exception ex){
					ex.printStackTrace();
				}
			}
		});
	}

	private void Succeed(){

		final View mView = getLayoutInflater().inflate(R.layout.activity_success, null);

		AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
		mBuilder.setView(mView);
		successDialog = mBuilder.create();
		successDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		successDialog.setCancelable(true);
		successDialog.show();
	}

	private void Failed(){

		final View mView = getLayoutInflater().inflate(R.layout.activity_failed, null);

		AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
		mBuilder.setView(mView);
		failDialog = mBuilder.create();
		failDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		failDialog.setCancelable(true);
		failDialog.show();
	}
}
