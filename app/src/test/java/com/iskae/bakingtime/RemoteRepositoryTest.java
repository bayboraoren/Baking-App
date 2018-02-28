package com.iskae.bakingtime;

import com.iskae.bakingtime.data.source.remote.BakingTimeService;
import com.iskae.bakingtime.data.source.remote.RemoteRecipesRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

/**
 * Created by iskae on 28.02.18.
 */
@RunWith(JUnit4.class)
public class RemoteRepositoryTest {
  private BakingTimeService bakingTimeService;
  private RemoteRecipesRepository remoteRecipesRepository;

  @Before
  public void init() {
    bakingTimeService = Mockito.mock(BakingTimeService.class);
    remoteRecipesRepository = new RemoteRecipesRepository(bakingTimeService);
  }

  @Test
  public void loadAllRecipes() {
    remoteRecipesRepository.getAllRecipes();
    Mockito.verify(bakingTimeService);
  }
}
