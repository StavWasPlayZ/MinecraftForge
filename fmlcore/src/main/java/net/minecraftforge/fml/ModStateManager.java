/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.fml;

import com.google.common.graph.GraphBuilder;
import cpw.mods.modlauncher.util.ServiceLoaderUtils;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.toposort.TopologicalSort;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings("UnstableApiUsage")
public class ModStateManager {
    private final EnumMap<ModLoadingPhase, List<IModLoadingState>> stateMap;

    public ModStateManager() {
        final var sp = ServiceLoader.load(FMLLoader.getGameLayer(), IModStateProvider.class);
        this.stateMap = ServiceLoaderUtils.streamWithErrorHandling(sp, sce->{})
                .map(IModStateProvider::getAllStates)
                .flatMap(List::stream)
                .collect(Collectors.groupingBy(IModLoadingState::phase, ()->new EnumMap<>(ModLoadingPhase.class), Collectors.toUnmodifiableList()));
    }

    public List<IModLoadingState> getStates(final ModLoadingPhase phase) {
        var nodes = stateMap.get(phase);
        var lookup = nodes.stream().collect(Collectors.toMap(IModLoadingState::name, Function.identity()));

        var graph = GraphBuilder.directed().allowsSelfLoops(false).<IModLoadingState>build();
        var dummy = ModLoadingState.empty("", "", phase);
        nodes.forEach(graph::addNode);
        graph.addNode(dummy);
        for (IModLoadingState node : nodes) {
            graph.putEdge(lookup.getOrDefault(node.previous(), dummy), node);
        }
        return TopologicalSort.topologicalSort(graph, Comparator.comparingInt(nodes::indexOf)).stream().filter(st->st!=dummy).toList();
    }

    public IModLoadingState findState(final String stateName) {
        return stateMap.values()
                .stream()
                .flatMap(List::stream)
                .filter(mls -> mls.name().equals(stateName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown IModLoadingState: " + stateName));
    }
}
