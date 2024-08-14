package nextstep.subway.path.infrastructure;

import lombok.RequiredArgsConstructor;
import nextstep.subway.common.SubwayErrorMessage;
import nextstep.subway.exception.NotConnectedStationException;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathType;
import nextstep.subway.path.domain.ShortestPathService;
import nextstep.subway.path.dto.SearchPathInfo;
import nextstep.subway.station.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JgraphtShortestPathService implements ShortestPathService {

//    @Override
//    public Path findShortestPath(List<Section> sections, SearchPathInfo searchPathInfo) {
//        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = initializeDijkstra(sections, searchPathInfo.getPathType());
//
//        try {
//            GraphPath<Station, DefaultWeightedEdge> shortestPath = dijkstraShortestPath.getPath(
//                    searchPathInfo.getSourceStation(),
//                    searchPathInfo.getTargetStation());
//
//            Pair<Long, Long> distanceAndDuration = findShortestDistanceAndDuration(sections, shortestPath);
//            return new Path(shortestPath.getVertexList(), distanceAndDuration.getFirst(), distanceAndDuration.getSecond());
//        } catch (NullPointerException | IllegalArgumentException e) {
//            throw new NotConnectedStationException(SubwayErrorMessage.NOT_CONNECTED_STATION);
//        }
//    }

    @Override
    public Path findShortestPath(List<Section> sections, SearchPathInfo searchPathInfo) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = initializeDijkstra(sections, searchPathInfo.getPathType());

        try {
            GraphPath<Station, DefaultWeightedEdge> shortestPath = dijkstraShortestPath.getPath(
                    searchPathInfo.getSourceStation(),
                    searchPathInfo.getTargetStation());

            List<Section> shortestSections = findShortestSections(sections, shortestPath);
            return new Path(shortestSections, shortestPath.getVertexList());

//            Pair<Long, Long> distanceAndDuration = findShortestDistanceAndDuration(sections, shortestPath);
//            return new Path(shortestPath.getVertexList(), distanceAndDuration.getFirst(), distanceAndDuration.getSecond());
        } catch (NullPointerException | IllegalArgumentException e) {
            throw new NotConnectedStationException(SubwayErrorMessage.NOT_CONNECTED_STATION);
        }
    }


//    private Pair<Long, Long> findShortestDistanceAndDuration(List<Section> sections, GraphPath<Station, DefaultWeightedEdge> shortestPath) {
//        List<Section> shortestSections = findShortestSections(sections, shortestPath);
//
//        return shortestSections.stream()
//                .map(section -> Pair.of(section.getDistance(), section.getDuration()))
//                .reduce(Pair.of(0L, 0L), (subPair, currentPair) -> Pair.of(
//                        subPair.getFirst() + currentPair.getFirst(),
//                        subPair.getSecond() + currentPair.getSecond())
//                );
//    }

    private List<Section> findShortestSections(List<Section> sections, GraphPath<Station, DefaultWeightedEdge> shortestPath) {
        List<Station> vertexList = shortestPath.getVertexList();

        List<Section> resultSection = new ArrayList<>();
        for (int i = 0; i < vertexList.size() - 1; i++) {
            Station upStation = vertexList.get(i);
            Station downStation = vertexList.get(i + 1);

            for (Section section : sections) {
                if (section.getUpStation().equals(upStation) && section.getDownStation().equals(downStation)) {
                    resultSection.add(section);
                }
            }
        }
        return resultSection;
    }


    private DijkstraShortestPath<Station, DefaultWeightedEdge> initializeDijkstra(List<Section> allSection, PathType pathType) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        allSection.forEach(section -> {
            addStationToGraph(graph, section);
            setEdgeWeight(graph, section, pathType);
        });
        return new DijkstraShortestPath<>(graph);
    }

    private void addStationToGraph(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Section section) {
        graph.addVertex(section.getUpStation());
        graph.addVertex(section.getDownStation());
    }

    private void setEdgeWeight(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Section section, PathType pathType) {
        graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), pathType.getWeight(section));
    }
}
