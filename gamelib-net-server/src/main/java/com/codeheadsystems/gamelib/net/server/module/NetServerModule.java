/*
 *   Copyright (c) 2023. Ned Wolpert <ned.wolpert@gmail.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

package com.codeheadsystems.gamelib.net.server.module;

import com.codeheadsystems.gamelib.net.module.NetCommonModule;
import com.codeheadsystems.gamelib.net.server.Authenticator;
import com.codeheadsystems.gamelib.net.server.GameListener;
import com.codeheadsystems.gamelib.net.server.model.ImmutableNetServerConfiguration;
import com.codeheadsystems.gamelib.net.server.model.NetServerConfiguration;
import dagger.Module;
import dagger.Provides;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.GlobalEventExecutor;
import java.io.File;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.net.ssl.SSLException;

/**
 * The type Net server module.
 */
@Module(includes = NetCommonModule.class)
public class NetServerModule {

  /**
   * The constant TLS_CERTIFICATE.
   */
  public static final String TLS_CERTIFICATE = "TLS_CERTIFICATE";
  /**
   * The constant TLS_PRIVATE_KEY.
   */
  public static final String TLS_PRIVATE_KEY = "TLS_PRIVATE_KEY";
  /**
   * The constant TIMER_EXECUTOR_SERVICE.
   */
  public static final String TIMER_EXECUTOR_SERVICE = "TimerExecutorService";
  private final NetServerConfiguration netServerConfiguration;
  private final Authenticator authenticator;
  private final GameListener gameListener;

  /**
   * Instantiates a new Net server module.
   *
   * @param authenticator the authenticator
   * @param gameListener  the game listener
   */
  public NetServerModule(final Authenticator authenticator,
                         final GameListener gameListener) {
    this(ImmutableNetServerConfiguration.builder().build(), authenticator, gameListener);
  }

  /**
   * Instantiates a new Net server module.
   *
   * @param netServerConfiguration the net server configuration
   * @param authenticator          the authenticator
   * @param gameListener           the game listener
   */
  public NetServerModule(final ImmutableNetServerConfiguration netServerConfiguration,
                         final Authenticator authenticator,
                         final GameListener gameListener) {
    this.netServerConfiguration = netServerConfiguration;
    this.authenticator = authenticator;
    this.gameListener = gameListener;
  }

  /**
   * Game listener game listener.
   *
   * @return the game listener
   */
  @Provides
  @Singleton
  public GameListener gameListener() {
    return gameListener;
  }

  /**
   * Net server configuration net server configuration.
   *
   * @return the net server configuration
   */
  @Provides
  @Singleton
  public NetServerConfiguration netServerConfiguration() {
    return netServerConfiguration;
  }

  /**
   * Channel group channel group.
   *
   * @param eventExecutor the event executor
   * @return the channel group
   */
  @Provides
  @Singleton
  public ChannelGroup channelGroup(final EventExecutor eventExecutor) {
    return new DefaultChannelGroup(eventExecutor);
  }

  /**
   * Event executor event executor.
   *
   * @return the event executor
   */
  @Provides
  @Singleton
  public EventExecutor eventExecutor() {
    // TODO: Get a real event executor
    return GlobalEventExecutor.INSTANCE;
  }

  /**
   * Authenticator authenticator.
   *
   * @return the authenticator
   */
  @Provides
  @Singleton
  public Authenticator authenticator() {
    return authenticator;
  }

  /**
   * Self signed certificate self signed certificate.
   *
   * @return the self signed certificate
   */
  @Provides
  @Singleton
  public SelfSignedCertificate selfSignedCertificate() {
    try {
      return new SelfSignedCertificate();
    } catch (CertificateException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Tls certificate file.
   *
   * @param ssc the ssc
   * @return the file
   */
  @Provides
  @Singleton
  @Named(TLS_CERTIFICATE)
  public File tlsCertificate(final SelfSignedCertificate ssc) {
    return ssc.certificate();
  }

  /**
   * Tls private key file.
   *
   * @param ssc the ssc
   * @return the file
   */
  @Provides
  @Singleton
  @Named(TLS_PRIVATE_KEY)
  public File tlsPrivateKey(final SelfSignedCertificate ssc) {
    return ssc.privateKey();
  }

  /**
   * Ssl context ssl context.
   *
   * @param certificate the certificate
   * @param privateKey  the private key
   * @return the ssl context
   */
  @Provides
  @Singleton
  public SslContext sslContext(@Named(TLS_CERTIFICATE) final File certificate,
                               @Named(TLS_PRIVATE_KEY) final File privateKey) {
    try {
      return SslContextBuilder.forServer(certificate, privateKey)
          .protocols("TLSv1.3")
          .ciphers(List.of("TLS_AES_256_GCM_SHA384"))
          .build();
    } catch (SSLException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Timer executor service scheduled thread pool executor.
   *
   * @param configuration the configuration
   * @return the scheduled thread pool executor
   */
  @Provides
  @Singleton
  @Named(TIMER_EXECUTOR_SERVICE)
  public ScheduledThreadPoolExecutor timerExecutorService(final NetServerConfiguration configuration) {
    return new ScheduledThreadPoolExecutor(configuration.timerExecutorPoolSize());
  }

}
