// Copyright 2000-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package org.jetbrains.idea.maven.statistics

import com.intellij.internal.statistic.eventLog.FeatureUsageData
import com.intellij.internal.statistic.service.fus.collectors.FUCounterUsageLogger
import com.intellij.internal.statistic.service.fus.collectors.FUSUsageContext
import com.intellij.internal.statistic.service.fus.collectors.UsageDescriptorKeyValidator
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.util.text.nullize

private const val GROUP_ID = "build.maven.actions"

class MavenActionsUsagesCollector {
  companion object {
    @JvmStatic
    fun trigger(project: Project?,
                featureId: String,
                place: String?,
                isFromContextMenu: Boolean,
                vararg additionalContextData: String) {
      if (project == null) return

      // preserve context data ordering
      val context = FUSUsageContext.create(
        place.nullize(true) ?: "undefined place",
        "fromContextMenu.$isFromContextMenu",
        *additionalContextData
      )
      FUCounterUsageLogger.getInstance().logEvent(
        project, GROUP_ID, UsageDescriptorKeyValidator.ensureProperKey(featureId), FeatureUsageData().addFeatureContext(context)
      )
    }

    @JvmStatic
    fun trigger(project: Project?, action: AnAction, event: AnActionEvent?, vararg additionalContextData: String) {
      trigger(project, action.javaClass.simpleName, event?.place, event?.isFromContextMenu ?: false, *additionalContextData)
    }

    @JvmStatic
    fun trigger(project: Project?, featureId: String, event: AnActionEvent?, vararg additionalContextData: String) {
      trigger(project, featureId, event?.place, event?.isFromContextMenu ?: false, *additionalContextData)
    }

    @JvmStatic
    fun trigger(project: Project?, feature: String) {
      if (project == null) return
      FUCounterUsageLogger.getInstance().logEvent(project, GROUP_ID, feature)
    }
  }
}
